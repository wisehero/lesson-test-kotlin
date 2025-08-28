package com.wisehero.infrastructure.db.post

import com.wisehero.domain.post.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostJpaRepository : JpaRepository<Post, Long> {
}
