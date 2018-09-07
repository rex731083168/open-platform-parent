package cn.ce.platform_service.apis.service;

import cn.ce.platform_service.apis.entity.ApiMock;
import cn.ce.platform_service.common.Result;

/**
 * @ClassName: IMockService
 * @Description: api测试mock数据
 * @create 2018/8/23 14:49/MKW
 * @update 2018/8/23 14:49/MKW/(说明。)....多次修改添加多个update
 */
public interface IMockService {

    Result inserOrUpdate(ApiMock apiMock);

    Result selectByVersionId(String versionId);

}
