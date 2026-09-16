package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    long countByAccountEnabledTrue();

    long countByEmailVerifiedTrue();

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderId(String providerId);

    @Query("""
            SELECT u
            FROM User u
            WHERE
                LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<User> searchUsers(
            @Param("search") String search,
            Pageable pageable
    );

    Page<User> findByRole(
            Role role,
            Pageable pageable
    );

    Page<User> findByAccountEnabled(
            boolean accountEnabled,
            Pageable pageable
    );

    @Query("""
            SELECT u
            FROM User u
            WHERE
                (
                    LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%'))
                )
            AND u.role = :role
            """)
    Page<User> searchUsersByRole(
            @Param("search") String search,
            @Param("role") Role role,
            Pageable pageable
    );

    @Query("""
            SELECT u
            FROM User u
            WHERE
                (
                    LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%'))
                )
            AND u.accountEnabled = :accountEnabled
            """)
    Page<User> searchUsersByAccountStatus(
            @Param("search") String search,
            @Param("accountEnabled") boolean accountEnabled,
            Pageable pageable
    );

    @Query("""
        SELECT u
        FROM User u
        WHERE
            (
                :search IS NULL
                OR :search = ''
                OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%'))
            )
        AND (:role IS NULL OR u.role = :role)
        AND (:accountEnabled IS NULL OR u.accountEnabled = :accountEnabled)
        AND (
            :student IS NULL
            OR (
                :student = true
                AND EXISTS (
                    SELECT s
                    FROM Student s
                    WHERE s.user = u
                )
            )
            OR (
                :student = false
                AND NOT EXISTS (
                    SELECT s
                    FROM Student s
                    WHERE s.user = u
                )
            )
        )
        """)
    Page<User> searchUsersWithFilters(
            @Param("search") String search,
            @Param("role") Role role,
            @Param("accountEnabled") Boolean accountEnabled,
            @Param("student") Boolean student,
            Pageable pageable
    );


}