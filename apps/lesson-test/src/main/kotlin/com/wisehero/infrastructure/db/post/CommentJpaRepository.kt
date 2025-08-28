package com.wisehero.infrastructure.db.post

import com.wisehero.domain.post.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentJpaRepository : JpaRepository<Comment, Long> {
}
