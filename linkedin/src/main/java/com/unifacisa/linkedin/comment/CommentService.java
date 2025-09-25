package com.unifacisa.linkedin.comment;

import com.unifacisa.linkedin.posts.Post;
import com.unifacisa.linkedin.posts.PostRepository;
import com.unifacisa.linkedin.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public Comment createComment(String text, Long postId, User author) {

        //  Busca o post que será comentado. Se não existir, lança um erro 404.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

        // 2. Cria a nova entidade de comentário
        Comment newComment = new Comment();
        newComment.setText(text);
        newComment.setAuthor(author);
        newComment.setPost(post);

        // 3. Salva no banco
        return commentRepository.save(newComment);
    }

    public List<Comment> findAllCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentário não encontrado"));

        // Verifica se o usuário é o autor do comentário OU o autor do post.
        if (!comment.getAuthor().getId().equals(currentUser.getId()) &&
                !comment.getPost().getAuthor().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para deletar este comentário");
        }

        commentRepository.delete(comment);
    }
}
