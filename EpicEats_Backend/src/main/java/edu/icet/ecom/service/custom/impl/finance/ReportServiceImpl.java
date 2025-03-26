package edu.icet.ecom.service.custom.impl.finance;

import edu.icet.ecom.dto.finance.Report;
import edu.icet.ecom.entity.finance.ReportEntity;
import edu.icet.ecom.repository.custom.finance.ReportRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.finance.ReportService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ReportServiceImpl implements ReportService {
	final private SuperServiceHandler<Report, ReportEntity> serviceHandler;

	public ReportServiceImpl (ReportRepository reportRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(reportRepository, mapper, Report.class, ReportEntity.class);
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
	public Response<Boolean> delete (Long id) {
		return this.serviceHandler.delete(id);
	}
}
