package messageboard.service;

import messageboard.Dto.CommentDto;
import messageboard.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment save(CommentDto commentDto);

    List<Comment> findAllComment(Long boardId);

    void deleteComment(CommentDto commentDto);

    Integer countComment(Long boardId);
}
