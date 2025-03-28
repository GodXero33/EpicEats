package edu.icet.ecom.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomHttpResponseMap {
	private CustomHttpResponseMap() {}

	public CustomHttpResponseMapBuilder builder () {
		return new CustomHttpResponseMapBuilder();
	}

	public static class CustomHttpResponseMapBuilder {
		private String[] keyArr;
		private Object[] valueArr;

		public CustomHttpResponseMapBuilder keys (String ...keys) {
			this.keyArr = keys;
			return this;
		}

		public CustomHttpResponseMapBuilder values (Object ...values) {
			this.valueArr = values;
			return this;
		}

		public Map<String, Object> build () {
			if (this.keyArr.length != this.valueArr.length) return Map.of();

			final Map<String, Object> map = new HashMap<>();

			for (int a = 0; a < this.keyArr.length; a++)
				if (this.keyArr[a] != null) map.put(this.keyArr[a], this.valueArr[a]);

			return map;
		}
	}
}
