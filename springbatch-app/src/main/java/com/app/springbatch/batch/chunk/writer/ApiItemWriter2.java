package com.app.springbatch.batch.chunk.writer;

import com.app.springbatch.batch.domain.ApiRequestVO;
import com.app.springbatch.batch.domain.ApiResponseVO;
import com.app.springbatch.service.AbstractApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

@Slf4j
public class ApiItemWriter2 extends FlatFileItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;

    public ApiItemWriter2(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {

        System.out.println("----------------------------------");
        items.forEach(item -> System.out.println("items = " + item));
        System.out.println("----------------------------------");

        ApiResponseVO response = apiService.service(items);
        System.out.println("response = " + response);

        // response setting
        items.forEach(item -> item.setApiResponseVO(response));

        /**
         * 처리 후, 해당 내용들을 파일로 생성하자.
         */
        super.setResource(new ClassPathResource("product2.txt"));
        super.open(new ExecutionContext()); // itemWriter 은 itemStream 구현하므로, executionContext 객체 전달
        super.setLineAggregator(new DelimitedLineAggregator<>()); // 구분자 방식 (default ,)
        super.setAppendAllowed(true); // 파일 이어붙이기
        super.write(items);
    }
}
