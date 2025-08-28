package com.wisehero.domain.post

import com.loopers.domain.BaseEntity
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "posts")
class Post(
    title: String,
    content: String,
    authorId: String,
) : BaseEntity() {

    init {
        if (title.isBlank()) throw CoreException(ErrorType.BAD_REQUEST, "제목이 비어있습니다.")
        if (content.isBlank()) throw CoreException(ErrorType.BAD_REQUEST, "내용이 비어있습니다.")
    }

    @Column(name = "title")
    var title: String = title
        protected set

    @Column(name = "content", columnDefinition = "TEXT")
    var content: String = content
        protected set

    @Column(name = "author_id")
    val authorId: String = authorId

    @Column(name = "view_count")
    var viewCount: Long = 0L
        protected set

    @OneToMany(
        mappedBy = "post",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    private val internalComments: MutableList<Comment> = mutableListOf()

    fun addComment(content: String, authorId: String): Comment {
        if (!isActive()) throw CoreException(ErrorType.NOT_FOUND, "삭제된 게시물에는 댓글을 달 수 없습니다.")

        val comment = Comment(this, content, authorId)
        internalComments.add(comment)
        return comment;
    }

    fun removeComment(commentId: Long, requestUserId: String) {
        val comment: Comment = internalComments.find { it.id == commentId }
            ?: throw CoreException(ErrorType.NOT_FOUND, "댓글을 찾을 수 없습니다.")

        if (!comment.isAuthor(requestUserId))
            throw CoreException(ErrorType.BAD_REQUEST, "댓글은 작성자 본인만 삭제할 수 있습니다.")
        comment.delete();
    }

    fun isActive(): Boolean = deletedAt == null

    fun isAuthor(authorId: String): Boolean = this.authorId == authorId


}
