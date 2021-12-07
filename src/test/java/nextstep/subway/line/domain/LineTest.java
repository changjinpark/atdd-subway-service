package nextstep.subway.line.domain;

import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.CannotUpdateException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {
    private Station 신사역;
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;
    private Station 수원역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        신사역 = new Station("신사역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        수원역 = new Station("수원역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @DisplayName("상행역에서 하행역 순으로 정렬하여 역을 가져온다.")
    @Test
    void getStationsOrderBy() {
        List<Station> stationsOrderBy = 신분당선.getStationsInOrder();
        assertThat(stationsOrderBy).isEqualTo(Arrays.asList(강남역, 광교역));
    }

    @DisplayName("역 사이에 새로운 역을 등록")
    @Test
    void addLineSection() {
        //when
        신분당선.addLineSection(강남역, 양재역, 3);
        List<Station> stations = 신분당선.getStationsInOrder();
        List<Section> sections = 신분당선.getSections();

        //then
        assertThat(stations).isEqualTo(Arrays.asList(강남역, 양재역, 광교역));
        assertThat(sections.get(0).getDistance()).isEqualTo(7);
    }

    @DisplayName("구간 등록 시에 새로운 역을 상행 종점으로 등록")
    @Test
    void addLineSection2() {
        //when
        신분당선.addLineSection(신사역, 강남역, 5);
        List<Station> stations = 신분당선.getStationsInOrder();
        List<Section> sections = 신분당선.getSections();

        //then
        assertThat(stations).isEqualTo(Arrays.asList(신사역, 강남역, 광교역));
        assertThat(sections.get(1).getDistance()).isEqualTo(5);
    }

    @DisplayName("구간 등록 시에 새로운 역을 하행 종점으로 등록")
    @Test
    void addLineSection3() {
        //when
        신분당선.addLineSection(광교역, 수원역, 8);
        List<Station> stations = 신분당선.getStationsInOrder();
        List<Section> sections = 신분당선.getSections();

        //then
        assertThat(stations).isEqualTo(Arrays.asList(강남역, 광교역, 수원역));
        assertThat(sections.get(1).getDistance()).isEqualTo(8);
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음")
    @Test
    void addLineSection_Exception1() {
        assertThatThrownBy(() -> {
            신분당선.addLineSection(강남역, 양재역, 10);
        }).isInstanceOf(CannotUpdateException.class).hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음")
    @Test
    void addLineSection_Exception2() {
        assertThatThrownBy(() -> {
            신분당선.addLineSection(강남역, 광교역, 5);
        }).isInstanceOf(CannotUpdateException.class).hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음")
    @Test
    void addLineSection_Exception3() {
        assertThatThrownBy(() -> {
            신분당선.addLineSection(신사역, 수원역, 5);
        }).isInstanceOf(CannotUpdateException.class).hasMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("노선의 구간 제거 - 노선과 노선 사이의 역을 제거")
    @Test
    void removeLineSection() {
        //given
        신분당선.addLineSection(신사역, 강남역, 5);

        //when
        신분당선.removeLineSection(강남역);

        //then
        assertThat(신분당선.getStationsInOrder()).isEqualTo(Arrays.asList(신사역, 광교역));
        assertThat(신분당선.getSections().get(0).getDistance()).isEqualTo(15);
    }

    @DisplayName("노선의 구간 제거 - 상행 종점역 제거")
    @Test
    void removeLineSection2() {
        //given
        신분당선.addLineSection(신사역, 강남역, 5);

        //when
        신분당선.removeLineSection(신사역);

        //then
        assertThat(신분당선.getStationsInOrder()).isEqualTo(Arrays.asList(강남역, 광교역));
        assertThat(신분당선.getSections().get(0).getDistance()).isEqualTo(10);
    }

    @DisplayName("노선의 구간 제거 - 하행 종점역 제거")
    @Test
    void removeLineSection3() {
        //given
        신분당선.addLineSection(광교역, 수원역, 5);

        //when
        신분당선.removeLineSection(수원역);

        //then
        assertThat(신분당선.getStationsInOrder()).isEqualTo(Arrays.asList(강남역, 광교역));
        assertThat(신분당선.getSections().get(0).getDistance()).isEqualTo(10);
    }

    @DisplayName("구간이 하나인 노선은 역을 제거할 수 없음")
    @Test
    void removeLineSectionException() {
        assertThatThrownBy(() -> {
            신분당선.removeLineSection(강남역);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessage("구간이 하나인 노선은 역을 제거할 수 없음");
    }
}
