package edu.icet.ecom.service.custom.impl.finance;

import edu.icet.ecom.dto.finance.Report;
import edu.icet.ecom.service.custom.finance.ReportService;
import edu.icet.ecom.util.Response;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ReportServiceImpl implements ReportService {
	@Override
	public Response<Report> get (Long id) {
		return null;
	}

	@Override
	public Response<List<Report>> getAll () {
		return null;
	}

	@Override
	public Response<Report> add (Report dto) {
		return null;
	}

	@Override
	public Response<Report> update (Report dto) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}
}
