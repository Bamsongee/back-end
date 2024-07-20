package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.kurlycrolling.KurlyCrollingResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KurlyCrollingService {
    private static Connection getJsoupConnection(String url) throws Exception {
        return Jsoup.connect(url);
    }

    public static Elements getJsoupElements(Connection connection, String url, String query) throws Exception {
        Connection conn = !ObjectUtils.isEmpty(connection) ? connection : getJsoupConnection(url);
        Elements result = null;
        result = conn.get().select(query);
        return result;
    }

    public ResponseDTO<List<HashMap<String, Object>>> curlyCrolling() throws Exception {
        String url = "https://www.melon.com/chart/index.htm";
        Document doc = Jsoup.connect(url).get();
        Elements elem = doc.select("tbody tr.lst50, tbody tr.lst100");

        System.out.println(elem);

        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;

        for(Element chartList : elem) {
            // 반복할 때마다 새로운 hashmap 생성
            map = new HashMap<String, Object>();

            // 순위 hashmap 에 저장
            map.put("rank", chartList.select("span.rank").html());
            // ------- 곡명, 아티스트, 앨범명도 같은 방식으로 추가 > 코드 생략 ----

            // 앨범 이미지 src 속성 hashmap 에 저장
            map.put("imgSrc", chartList.select("div div div div div span img.image_typeAll img").attr("src"));

            // 이미지 a 태그 href 에서 숫자만 출력 (앨범 번호)
            /*
            Elements albHref = chartList.select("a.image_typeAll");
            String[] albNumber = albHref.attr("href").split("\\D+");
            String albNo = "";
            for (String no : albNumber) {
                if (!no.isEmpty()) {
                    albNo += no;
                }
            }
            map.put("albNo", albNo);

            // list 에 hashmap 추가 */
            list.add(map);
        }

        ResponseDTO response = new ResponseDTO(HttpStatus.OK.value(), "크롤링 성공", list);
        return response;

    }
}
