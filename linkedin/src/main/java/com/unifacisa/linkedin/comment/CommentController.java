package com.unifacisa.linkedin.comment;

import com.unifacisa.linkedin.comment.dto.CommentCreateDTO;
import com.unifacisa.linkedin.comment.dto.CommentResponseDTO;
import com.unifacisa.linkedin.user.User;
import com.unifacisa.linkedin.user.UserDTO;
import com.unifacisa.linkedin.user.UserServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @Autowired
    private UserServices userServices;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            System.out.println(">>>>>>>. Entrou no createComment do CommentController.");

            if (userDetails == null) {
                System.err.println("!!!!!!!!!! ERRO: userDetails ta vindo NULO.");

                //.build() para retornar um corpo vazio do tipo correto
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String authorEmail = userDetails.getUsername();
            System.out.println(">>>>>>>. E-mail do autor foi : " + authorEmail);

            User author = userServices.findByEmail(authorEmail);
            System.out.println(">>>>>>>  User' do autor encontrado. ID: " + author.getId());

            Comment newComment = commentService.createComment(dto.getText(), postId, author);
            System.out.println(">>>>>>> Comentário criado pelo serviço. ID: " + newComment.getId());

            if (modelMapper == null) {
                System.err.println("!!!!!!!!!!!!! ModelMapper está NULO.");
               //.build() para retornar um corpo vazio
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            UserDTO authorDTO = modelMapper.map(author, UserDTO.class);
            System.out.println(">>>>>> Mapeamento do autor para UserDTO concluído.");

            CommentResponseDTO response = new CommentResponseDTO(newComment, authorDTO);
            System.out.println(">>> 6. DTO response criado. Enviando 201 Created.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("!!!!!!!!!! ERRO DENTRO DO CommentController !!!!!!!!!!");
            System.err.println("Tipo da Exceção: " + e.getClass().getName());
            System.err.println("Mensagem da Exceção: " + e.getMessage());
            e.printStackTrace();
            //.build() para retornar um corpo vazio
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> listCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.findAllCommentsByPostId(postId);

        List<CommentResponseDTO> response = comments.stream().map(comment -> {
            UserDTO authorDTO = modelMapper.map(comment.getAuthor(), UserDTO.class);
            return new CommentResponseDTO(comment, authorDTO);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User currentUser = userServices.findByEmail(userDetails.getUsername());
        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
