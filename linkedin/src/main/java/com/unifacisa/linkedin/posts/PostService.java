package com.unifacisa.linkedin.posts;

import com.unifacisa.linkedin.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(String content, User author){
        Post newPost = new Post();
        newPost.setContent(content);
        newPost.setAuthor(author);
        return postRepository.save(newPost);
    }

    public Page<Post> listAllPosts(Pageable pageable){
        return postRepository.findAll(pageable) ;
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));
    }


    public void deletePost(Long postId, User currentUser) {
        // 1. Busca o post no banco. Se não encontrar, lança um erro 404.
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

        // 2. Verifica se o usuário logado é o autor do post.
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            // Se não for o autor, lança um erro 403 Forbidden (Proibido).
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para deletar este post");
        }

        // 3. Se a verificação passar, deleta o post.
        postRepository.delete(post);
    }
}
