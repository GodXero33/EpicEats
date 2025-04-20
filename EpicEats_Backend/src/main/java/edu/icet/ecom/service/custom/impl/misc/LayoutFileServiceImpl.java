package edu.icet.ecom.service.custom.impl.misc;

import edu.icet.ecom.service.custom.misc.LayoutFileService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@Primary
public class LayoutFileServiceImpl implements LayoutFileService {
	private final Path layoutPath = Paths.get("layouts/shop-table-plan.json");
	private final Logger logger;

	public LayoutFileServiceImpl (Logger logger) {
		this.logger = logger;

		try {
			Files.createDirectories(this.layoutPath.getParent());
		} catch (IOException exception) {
			this.logger.error(exception.getMessage());
		}
	}

	@Override
	public boolean saveLayout (String json) {
		try {
			Files.writeString(this.layoutPath, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			return true;
		} catch (IOException exception) {
			this.logger.error(exception.getMessage());
			return false;
		}
	}

	@Override
	public String loadLayout () {
		try {
			return Files.readString(this.layoutPath);
		} catch (IOException exception) {
			this.logger.error(exception.getMessage());
			return null;
		}
	}
}
