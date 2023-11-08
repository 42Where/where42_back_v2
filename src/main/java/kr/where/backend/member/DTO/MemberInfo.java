//package kr.where.backend.member.DTO;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import kr.where.backend.member.Member;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Schema(description = "42where 맴버 정보")
//public class MemberInfo {
//    @Schema(description = "Id")
//    private Long id;
//    @Schema(description = "name")
//    private String name;
//    @Schema(description = "image URL")
//    private String img;
//    @Schema(description = "status messege")
//    private String msg;
//    @Schema(description = "맴버의 클러스터 위치")
//    private Locate locate;
//    @Schema(description = "클러스터 안에 있는지 밖에 있는지의 상태")
//    private int inOrOut;
//    @Schema(description = "평가 중인지에 대한 상태")
//    private int eval;
//
////    public MemberInfo(Member member) {
////        this.id = member.getId();
////        this.name = member.getName();
////        this.img = member.getImg();
////        this.msg = member.getMsg();
////        this.locate = member.getLocate();
////        this.inOrOut = member.getInOrOut();
////        this.eval = member.getEvaling();
////    }
//}