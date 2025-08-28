package com.wisehero.domain.post

import com.loopers.domain.BaseEntity
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "comments")
class Comment(
    post: Post,
    content: String,
    commentAuthorId: String,
) : BaseEntity() {

    init {
        if (content.isBlank()) throw CoreException(ErrorType.BAD_REQUEST, "댓글 내용이 비어있습니다.")
        if(content.length > 1000) throw CoreException(ErrorType.BAD_REQUEST, "댓글 내용이 너무 깁니다.")
        if (commentAuthorId.isBlank()) throw CoreException(ErrorType.BAD_REQUEST, "작성자 ID가 비어있습니다.")
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post = post

    @Column(name = "content")
    val content: String = content

    @Column(name = "author_id")
    val commentAuthorId: String = commentAuthorId

    fun isPostAuthor() : Boolean = post.isAuthor(commentAuthorId)

    fun isAuthor(userId: String): Boolean = commentAuthorId == userId
}
