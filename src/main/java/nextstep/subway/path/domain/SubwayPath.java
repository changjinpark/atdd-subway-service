package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class SubwayPath {
    private List<Station> stations;
    private int distance;
    private Fare fare;

    public SubwayPath() {
    }

    public SubwayPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare.of(distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }
}
