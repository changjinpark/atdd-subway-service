package nextstep.subway.line.domain;

import nextstep.subway.exception.CannotUpdateException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private Distance distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    public void updateUpStation(Station station, int newDistance) {
        this.distance.validateLargerThan(newDistance);
        this.upStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public void updateDownStation(Station station, int newDistance) {
        this.distance.validateLargerThan(newDistance);
        this.downStation = station;
        this.distance = this.distance.minus(newDistance);
    }

    public boolean equalsDownStation(Station upStation) {
        return this.downStation.equals(upStation);
    }

    public boolean equalsUpStation(Station downStation) {
        return this.upStation.equals(downStation);
    }
}
