package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseEnrollmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.EnrollmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Course;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.CourseEnrollment;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Student;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.CourseEnrollmentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.CourseRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderItemRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.CourseEnrollmentTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseEnrollmentService {

    private final CourseEnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StudentService studentService;
    private final CourseEnrollmentTransformer enrollmentTransformer;


    // ============================================================
    // CREATE ENROLLMENT
    // ============================================================

    @Transactional
    public CourseEnrollmentResponse createEnrollment(
            Long userId,
            Long courseId,
            Long orderId
    ) {

        // --------------------------------------------------------
        // 1. Find course
        // --------------------------------------------------------

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id: " + courseId
                        )
                );


        // --------------------------------------------------------
        // 2. Check course is active
        // --------------------------------------------------------

        if (!course.isActive()) {

            throw new BadRequestException(
                    "Course is not active"
            );
        }


        // --------------------------------------------------------
        // 3. Find order
        // --------------------------------------------------------

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );


        // --------------------------------------------------------
        // 4. Make sure order belongs to logged-in user
        // --------------------------------------------------------

        if (
                order.getUser() == null ||
                        !order.getUser().getUserId().equals(userId)
        ) {

            throw new BadRequestException(
                    "You are not allowed to use this order"
            );
        }


        // --------------------------------------------------------
        // 5. Order must be PAID
        // --------------------------------------------------------

        if (order.getStatus() != OrderStatus.PAID) {

            throw new BadRequestException(
                    "Course cannot be enrolled because order is not paid"
            );
        }


        // --------------------------------------------------------
        // 6. Verify order contains this course
        // --------------------------------------------------------

        OrderItem orderItem =
                orderItemRepository.findOrderItemForProduct(
                        orderId,
                        ProductType.COURSE,
                        courseId
                ).orElseThrow(() ->
                        new BadRequestException(
                                "This order does not contain the selected course"
                        )
                );


        // --------------------------------------------------------
        // 7. Validate product quantity
        // --------------------------------------------------------

        if (
                orderItem.getQuantity() == null ||
                        orderItem.getQuantity() <= 0
        ) {

            throw new BadRequestException(
                    "Invalid course quantity in order"
            );
        }


        // --------------------------------------------------------
        // 8. Create Student if not already exists
        // --------------------------------------------------------

        Student student =
                studentService.createIfNotExists(userId);


        // --------------------------------------------------------
        // 9. Check existing enrollment
        // --------------------------------------------------------

        return enrollmentRepository
                .findForUpdate(
                        student.getStudentId(),
                        courseId
                )
                .map(enrollmentTransformer::toResponse)

                // ------------------------------------------------
                // 10. Create new enrollment
                // ------------------------------------------------
                .orElseGet(() -> {

                    CourseEnrollment enrollment =
                            CourseEnrollment.builder()
                                    .student(student)
                                    .course(course)
                                    .order(order)
                                    .status(EnrollmentStatus.ACTIVE)
                                    .build();

                    CourseEnrollment savedEnrollment =
                            enrollmentRepository.save(enrollment);

                    return enrollmentTransformer.toResponse(
                            savedEnrollment
                    );
                });
    }


    // ============================================================
    // MY ENROLLMENTS
    // ============================================================

    @Transactional(readOnly = true)
    public List<CourseEnrollmentResponse> getMyEnrollments(
            Long userId
    ) {

        Student student =
                studentRepository.findByUser_UserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student record not found"
                                )
                        );

        return enrollmentRepository
                .findStudentEnrollments(
                        student.getStudentId()
                )
                .stream()
                .map(enrollmentTransformer::toResponse)
                .toList();
    }


    // ============================================================
    // MY ACTIVE ENROLLMENTS
    // ============================================================

    @Transactional(readOnly = true)
    public List<CourseEnrollmentResponse> getMyActiveEnrollments(
            Long userId
    ) {

        Student student =
                studentRepository.findByUser_UserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student record not found"
                                )
                        );

        return enrollmentRepository
                .findStudentEnrollmentsByStatus(
                        student.getStudentId(),
                        EnrollmentStatus.ACTIVE
                )
                .stream()
                .map(enrollmentTransformer::toResponse)
                .toList();
    }
}