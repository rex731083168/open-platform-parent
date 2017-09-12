package cn.ce.core;

import java.io.Serializable;

import com.mongodb.BasicDBObject;

/**
 * 
 * @ClassName: IEntity
 * @Description: 基本接口
 * @author dingjia@300.cn
 *
 */
public interface IEntity extends Serializable {
    public BasicDBObject toBasicDBObject();
    public void parse(BasicDBObject b);
}
