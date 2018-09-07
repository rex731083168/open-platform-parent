package cn.ce.platform_service.apis.dao;

import cn.ce.platform_service.apis.entity.ApiMock;

/**
 * @ClassName: IMysqlApiMockDao
 * @Description: TODO
 * @create 2018/8/24 11:23/MKW
 * @update 2018/8/24 11:23/MKW/(说明。)....多次修改添加多个update
 */
public interface IMysqlApiMockDao {

    int insert(ApiMock apiMock);

    int deleteByVersionId(String versionId);

    ApiMock selectByVersionId(String versionId);
}
