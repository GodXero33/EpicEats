package edu.icet.ecom.service.custom.impl.merchandise;

import edu.icet.ecom.dto.merchandise.AllSalesPackages;
import edu.icet.ecom.dto.merchandise.SalesPackage;
import edu.icet.ecom.dto.merchandise.SuperSalesPackage;
import edu.icet.ecom.entity.merchandise.SuperSalesPackageEntity;
import edu.icet.ecom.repository.custom.merchandise.SalesPackageRepository;
import edu.icet.ecom.service.custom.merchandise.SalesPackageService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class SalesPackageServiceImpl implements SalesPackageService {
	private final SalesPackageRepository salesPackageRepository;
	private final ModelMapper mapper;

	@Override
	public Response<Boolean> isNameExist (String name) {
		return this.salesPackageRepository.isNameExist(name);
	}

	@Override
	public Response<SuperSalesPackage> getAllStructured () {
		final Response<SuperSalesPackageEntity> response = this.salesPackageRepository.getAllStructured();

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), AllSalesPackages.class) :
			null, response.getStatus());
	}

	@Override
	public Response<SuperSalesPackage> get (Long id) {
		final Response<SuperSalesPackageEntity> response = this.salesPackageRepository.get(id);

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), SalesPackage.class) :
			null, response.getStatus());
	}

	@Override
	public Response<List<SuperSalesPackage>> getAll () {
		return new Response<>(null, ResponseType.SERVER_ERROR);
	}

	@Override
	public Response<SuperSalesPackage> add (SuperSalesPackage dto) {
		final Response<SuperSalesPackageEntity> response = this.salesPackageRepository.add(this.mapper.map(dto, SuperSalesPackageEntity.class));

		return new Response<>(response.getStatus() == ResponseType.CREATED ?
			this.mapper.map(response.getData(), SuperSalesPackage.class) :
			null, response.getStatus());
	}

	@Override
	public Response<SuperSalesPackage> update (SuperSalesPackage dto) {
		final Response<SuperSalesPackageEntity> response = this.salesPackageRepository.update(this.mapper.map(dto, SuperSalesPackageEntity.class));

		return new Response<>(response.getStatus() == ResponseType.UPDATED ?
			this.mapper.map(response.getData(), SuperSalesPackage.class) :
			null, response.getStatus());
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.salesPackageRepository.delete(id);
	}
}
