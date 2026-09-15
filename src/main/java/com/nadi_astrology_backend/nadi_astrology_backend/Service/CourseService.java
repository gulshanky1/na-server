package com.nadi_astrology_backend.nadi_astrology_backend.Service;


import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CourseRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CourseResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.DuplicateResourceException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Course;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.CourseRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.CourseTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseTransformer courseTransformer;
    private final ProductSyncService productSyncService;
    private final CloudinaryService cloudinaryService;


    // =========================================================
    // CREATE COURSE
    // =========================================================

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {

        String title = request.getTitle().trim();

        if (courseRepository.existsByTitleIgnoreCase(title)) {
            throw new DuplicateResourceException(
                    "Course with this title already exists"
            );
        }

        Course course = courseTransformer.toEntity(request);

        Course savedCourse = courseRepository.save(course);

        // Automatically create Product for this Course
        productSyncService.createOrUpdateProduct(
                ProductType.COURSE,
                savedCourse.getCourseId(),
                savedCourse.getTitle(),
                savedCourse.getShortDescription(),
                savedCourse.getImageUrl(),
                savedCourse.getPrice(),
                savedCourse.isActive()
        );

        return courseTransformer.toResponse(savedCourse);
    }


    // =========================================================
    // GET COURSE BY ID - PUBLIC
    // =========================================================

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found"
                        )
                );

        return courseTransformer.toResponse(course);
    }


    // =========================================================
    // GET ACTIVE COURSES - PUBLIC
    // =========================================================

    @Transactional(readOnly = true)
    public Page<CourseResponse> getActiveCourses(
            Pageable pageable
    ) {

        return courseRepository
                .findByActiveTrue(pageable)
                .map(courseTransformer::toResponse);
    }


    // =========================================================
    // GET ALL COURSES - ADMIN
    // =========================================================

    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllCourses(
            Pageable pageable
    ) {

        return courseRepository
                .findAll(pageable)
                .map(courseTransformer::toResponse);
    }


    // =========================================================
    // UPDATE COURSE - ADMIN
    // =========================================================

    @Transactional
    public CourseResponse updateCourse(
            Long courseId,
            CourseRequest request
    ) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found"
                        )
                );

        String newTitle = request.getTitle().trim();

        if (!course.getTitle().equalsIgnoreCase(newTitle)
                && courseRepository.existsByTitleIgnoreCase(newTitle)) {

            throw new DuplicateResourceException(
                    "Course with this title already exists"
            );
        }

        courseTransformer.updateEntity(course, request);

        Course savedCourse = courseRepository.save(course);

        // Keep Product synchronized
        productSyncService.createOrUpdateProduct(
                ProductType.COURSE,
                savedCourse.getCourseId(),
                savedCourse.getTitle(),
                savedCourse.getShortDescription(),
                savedCourse.getImageUrl(),
                savedCourse.getPrice(),
                savedCourse.isActive()
        );

        return courseTransformer.toResponse(savedCourse);
    }


    // =========================================================
    // DEACTIVATE COURSE - ADMIN
    // =========================================================

    @Transactional
    public CourseResponse deactivateCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found"
                        )
                );

        course.setActive(false);

        Course savedCourse = courseRepository.save(course);

        // Deactivate corresponding Product
        productSyncService.deactivateProduct(
                ProductType.COURSE,
                courseId
        );

        return courseTransformer.toResponse(savedCourse);
    }


    // =========================================================
    // UPLOAD COURSE IMAGE - ADMIN
    // =========================================================

    @Transactional
    public CourseResponse uploadCourseImage(
            Long courseId,
            MultipartFile file
    ) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found"
                        )
                );

        // Upload image to Cloudinary
        String imageUrl = cloudinaryService.uploadImage(file);

        // Save Cloudinary URL in Course
        course.setImageUrl(imageUrl);

        Course savedCourse = courseRepository.save(course);

        // Synchronize Product image
        productSyncService.createOrUpdateProduct(
                ProductType.COURSE,
                savedCourse.getCourseId(),
                savedCourse.getTitle(),
                savedCourse.getShortDescription(),
                savedCourse.getImageUrl(),
                savedCourse.getPrice(),
                savedCourse.isActive()
        );

        return courseTransformer.toResponse(savedCourse);
    }
}