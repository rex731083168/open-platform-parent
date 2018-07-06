package cn.ce.platform_service.dbSwitch;

import cn.ce.platform_service.common.DataSourceEnum;

public class DataSourceTypeManager {
	private static final ThreadLocal<DataSourceEnum> dataSourceTypes = new ThreadLocal<DataSourceEnum>() {
		@Override
		protected DataSourceEnum initialValue() {
			return DataSourceEnum.PROD;
		}
	};

	public static DataSourceEnum get() {
		return dataSourceTypes.get();
	}

	public static void set(DataSourceEnum dataSourceType) {
		dataSourceTypes.set(dataSourceType);
	}

	public static void reset() {
		dataSourceTypes.set(DataSourceEnum.PROD);
	}
}