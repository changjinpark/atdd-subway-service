package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
	private final LineRepository lineRepository;
	private final StationService stationService;

	@Transactional
	public LineResponse saveLine(LineRequest request) {
		Station upStation = stationService.findById(request.getUpStationId());
		Station downStation = stationService.findById(request.getDownStationId());
		Line persistLine = lineRepository.save(
			new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
		List<StationResponse> stations = persistLine.getStations()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		return LineResponse.of(persistLine, stations);
	}

	public List<LineResponse> findLines() {
		List<Line> persistLines = lineRepository.findAll();
		return persistLines.stream()
			.map(line -> {
				List<StationResponse> stations = line.getStations()
					.stream()
					.map(StationResponse::of)
					.collect(Collectors.toList());
				return LineResponse.of(line, stations);
			})
			.collect(Collectors.toList());
	}

	public Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	public LineResponse findLineResponseById(Long id) {
		Line persistLine = findLineById(id);
		List<StationResponse> stations = persistLine.getStations()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		return LineResponse.of(persistLine, stations);
	}

	@Transactional
	public void updateLine(Long id, LineRequest lineUpdateRequest) {
		Line persistLine = lineRepository.findById(id)
			.orElseThrow(RuntimeException::new);
		persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	@Transactional
	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);
		Station upStation = stationService.findById(request.getUpStationId());
		Station downStation = stationService.findById(request.getDownStationId());
		line.addSection(new Section(line, upStation, downStation, request.getDistance()));
	}

	@Transactional
	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station targetStation = stationService.findById(stationId);
		line.removeLineStation(targetStation);
	}
}