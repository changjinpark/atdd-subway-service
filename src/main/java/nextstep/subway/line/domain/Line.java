package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private int surcharge;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.addLineSection(this, upStation, downStation, distance);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, int surcharge) {
        this.name = name;
        this.color = color;
        sections.addLineSection(this, upStation, downStation, distance);
        this.surcharge = surcharge;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public List<Station> getStationsInOrder() {
        return sections.getStationsInOrder();
    }

    public void addLineSection(Station upStation, Station downStation, int distance) {
        sections.addLineSection(this, upStation, downStation, distance);
    }

    public void removeLineSection(Station station) {
        sections.removeLineSection(this, station);
    }

    public int getSurcharge() {
        return surcharge;
    }
}
