package com.unifacisa.linkedin.posts;

import com.unifacisa.linkedin.posts.dto.PostCreateDTO;
import com.unifacisa.linkedin.posts.dto.PostResponseDTO;
import com.unifacisa.linkedin.user.User;
import com.unifacisa.linkedin.user.UserDTO;
import com.unifacisa.linkedin.user.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserServices userServices;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody PostCreateDTO postCreateDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // --- BLOCO DE DEBUG ---
        try {

            System.out.println(">>> 1. Entrou no createPost do PostController.");

            String authorEmail = userDetails.getUsername();
            System.out.println(">>> 2. E-mail do autor extraído: " + authorEmail);

            User author = userServices.findByEmail(authorEmail);
            System.out.println(">>> 3.  'User' do autor encontrada no banco. ID: " + author.getId());

            Post newPost = postService.createPost(postCreateDTO.getContent(), author);
            System.out.println(">>> 4. Post criado pelo serviço. ID do Post: " + newPost.getId());

            UserDTO authorDTO = modelMapper.map(author, UserDTO.class);
            System.out.println(">>> 5. Mapeamento do autor para UserDTO concluído.");

            PostResponseDTO response = new PostResponseDTO(newPost, authorDTO);
            System.out.println(">>> 6. Enviando 201 Created.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("!!!!!!!!!! ERRO DENTRO DO PostController !!!!!!!!!!");
            System.err.println("Tipo da Exceção: " + e.getClass().getName());
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // --- FIM DO BLOCO DE DEBUG ---
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> listPosts(Pageable pageable) {
        Page<Post> postPage = postService.listAllPosts(pageable);

        Page<PostResponseDTO> responsePage = postPage.map(post -> {
            UserDTO authorDTO = modelMapper.map(post.getAuthor(), UserDTO.class);
            return new PostResponseDTO(post, authorDTO);
        });

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        Post postEntity = postService.findById(id);
        UserDTO authorDTO = modelMapper.map(postEntity.getAuthor(), UserDTO.class);
        PostResponseDTO response = new PostResponseDTO(postEntity, authorDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User currentUser = userServices.findByEmail(userDetails.getUsername());
        postService.deletePost(postId, currentUser);

        // Retorna 204 No Content
        return ResponseEntity.noContent().build();
    }
}
