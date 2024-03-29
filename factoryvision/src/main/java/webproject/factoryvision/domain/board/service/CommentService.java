package webproject.factoryvision.domain.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webproject.factoryvision.domain.board.dto.CommentRequest;
import webproject.factoryvision.domain.board.dto.CommentResponse;
import webproject.factoryvision.domain.board.entity.Board;
import webproject.factoryvision.domain.board.entity.Comment;
import webproject.factoryvision.domain.board.mapper.CommentMapper;
import webproject.factoryvision.domain.board.repository.BoardRepository;
import webproject.factoryvision.domain.board.repository.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Builder
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentMapper commentMapper;

    public void WriteComment(Long id, CommentRequest request) {
        Board board = boardRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("게시판 정보가 없습니다."));
        Comment result = Comment.builder()
                .content(request.getContent())
                .nickname(request.getNickname())
                .board(board)
                .build();
        commentRepository.save(result);
    }

    public List<CommentResponse> getCommentsById(Long id){
        Board board = boardRepository.findById(id).orElseThrow();;
        List<Comment> comments = commentRepository.findByBoard(board);
        return comments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    public void DeleteComment(Long postId, Long commentId) {
        Optional<Board> board = boardRepository.findById(postId);
        if (board.isPresent()) {
            commentRepository.deleteById(commentId);
        } else {
            throw new EntityNotFoundException("댓글에 해당되는 게시글이 없습니다.");
        }
    }
}
