package capstone.be.global.dto.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ResponseService {

    // 단일건 결과 처리 메소드
    public CommonResult getSingleResult() {
        CommonResult result = new CommonResult();
        return result;
    }

    // 복수건 결과 처리 메서드
    public ListResult getListResult(List list) {
        ListResult result = new ListResult<>();
        result.setList(list);
        return result;
    }


    // 실패 결과만 처리
    public CommonResult getFailResult(String code) {
        FailResult result = new FailResult();
        setFailResult(result, code);
        return result;
    }


    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private void setFailResult(FailResult result, String code) {
        result.setCode(code);
    }
}