package edu.icet.ecom.service.custom.impl.finance;

import edu.icet.ecom.dto.finance.AllReports;
import edu.icet.ecom.dto.finance.Report;
import edu.icet.ecom.dto.finance.ReportLite;
import edu.icet.ecom.dto.finance.ReportsByEmployee;
import edu.icet.ecom.entity.finance.AllReportsEntity;
import edu.icet.ecom.entity.finance.ReportLiteEntity;
import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.entity.finance.ReportsByEmployeeEntity;
import edu.icet.ecom.repository.custom.finance.ReportRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.finance.ReportService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ReportServiceImpl implements ReportService {
	private final SuperServiceHandler<Report, ReportEntity> serviceHandler;
	private final ReportRepository reportRepository;
	private final ModelMapper mapper;

	public ReportServiceImpl (ReportRepository reportRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(reportRepository, mapper, Report.class, ReportEntity.class);
		this.reportRepository = reportRepository;
		this.mapper = mapper;
	}

	@Override
	public Response<Report> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<Report>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<Report> add (Report dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<Report> update (Report dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Report> add (ReportLite report) {
		final Response<ReportEntity> response = this.reportRepository.add(this.mapper.map(report, ReportLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.CREATED ?
			this.mapper.map(response.getData(), Report.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<Report> update (ReportLite report) {
		final Response<ReportEntity> response = this.reportRepository.update(this.mapper.map(report, ReportLiteEntity.class));

		return new Response<>(response.getStatus() == ResponseType.UPDATED ?
			this.mapper.map(response.getData(), Report.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<AllReports> getAllStructured () {
		final Response<AllReportsEntity> response = this.reportRepository.getAllStructured();

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), AllReports.class) :
			null
			, response.getStatus());
	}

	@Override
	public Response<Object> deleteByEmployeeId (Long employeeId) {
		return this.reportRepository.deleteByEmployeeId(employeeId);
	}

	@Override
	public Response<ReportsByEmployee> getAllByEmployeeId (Long employeeId) {
		final Response<ReportsByEmployeeEntity> response = this.reportRepository.getAllByEmployeeId(employeeId);

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), ReportsByEmployee.class) :
			null
			, response.getStatus());
	}
}
