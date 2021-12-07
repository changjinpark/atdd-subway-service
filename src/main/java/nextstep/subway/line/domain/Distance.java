package nextstep.subway.line.domain;

import nextstep.subway.exception.CannotUpdateException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역 간 거리는 0보다 커야합니다");
        }
        this.distance = distance;
    }

    public void validateLargerThan(int newDistance) {
        if (this.distance <= newDistance) {
            throw new CannotUpdateException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public Distance minus(int newDistance) {
        return new Distance(this.distance - newDistance);
    }

    public int getDistance() {
        return this.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
