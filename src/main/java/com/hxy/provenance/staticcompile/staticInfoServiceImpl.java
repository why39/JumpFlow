package com.hxy.provenance.staticcompile;

import com.hxy.modules.common.utils.Result;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hxy.provenance.neo4j.Neo4jFinalUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hxy.provenance.neo4j.Neo4jFinalUtil;

@Service("staticInfoService")
public class staticInfoServiceImpl implements staticInfoService,AutoCloseable{

    @Autowired staticInfoDao staticinfodao;


    private static Driver driver = null;

//    public staticInfoServiceImpl(String uri, String user, String password )
////    {
////        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
////    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    @Override
    public staticInfoEntity querySingleInfo(String nid) {
        return staticinfodao.querySingleInfo(nid);
    }

    @Override
    public List<staticInfoEntity> queryCategoryInfo(String category) {
        return staticinfodao.queryCategoryInfo(category);
    }

    @Override
    public Result insertNode(String category, String caseId) {
//        List<staticInfoEntity> infoList = new ArrayList();
//        infoList = queryCategoryInfo(category);
//        for (staticInfoEntity sEntity : infoList) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("cate", sEntity.getNid());
//            map.put("cate", sEntity.getCategory());
//            map.put("name", sEntity.getName());
//            map.put("type", sEntity.getType());
//            Neo4jFinalUtil.createKeyValues(caseId, "static", sEntity.getPid(), "rel", false, map);
//        }

        try {
            driver = Neo4jFinalUtil.createDrive();
            Session session0 = driver.session();
            session0.writeTransaction(new TransactionWork<Object>() {
                @Override
                public Void execute(Transaction tx) {
                        tx.run("CREATE CONSTRAINT ON (a:静态) "+
                                "ASSERT a.uid IS UNIQUE"
                        );
                    return null;
                }
            });
            session0.close();

            Session session1 = driver.session();
            session1.writeTransaction(new TransactionWork<Object>() {
                @Override
                public Void execute(Transaction tx) {
                    List<staticInfoEntity> infoList = new ArrayList();
                    infoList = queryCategoryInfo(category);
                    for(staticInfoEntity sEntity : infoList){
                        tx.run("CREATE (a:静态) " +
                                "SET a.category = " +"'" +  sEntity.getCategory() +"'" +
                                "SET a.nid = " +"'" +  sEntity.getNid() +"'" +
                                "SET a.name = " +"'" +  sEntity.getName() +"'" +
                                "SET a.type = " +"'" +  sEntity.getType() +"'" +
                                "SET a.bmsah = " +"'" +  caseId +"'"+
                                "SET a.uid = " +"'" +  caseId +  sEntity.getNid() +"'"
                        );
                    }
                    return null;
                }
            });
            session1.close();
            Session session2 = driver.session();
            session2.writeTransaction(new TransactionWork<Object>() {
                @Override
                public List<InternalRelationship> execute(Transaction tx) {
                    List<staticInfoEntity> infoList = new ArrayList();
                    infoList = queryCategoryInfo(category);
                    for(staticInfoEntity sEntity : infoList){
                        if(sEntity.getPid() != null){
                            tx.run("MATCH (a:静态),(b:静态)" +
                                    " WHERE a.nid = "  +"'" +  sEntity.getPid() +"'"+
                                    " AND a.bmsah = "  +"'" +  caseId +"'"+
                                    " AND b.nid = "  +"'" +  sEntity.getNid() +"'"+
                                    " AND b.bmsah = "  +"'" +  caseId +"'"+
                                    " CREATE (b)-[r:依赖]-> (a)"
                            );
                        }
                    }
                    return null;
                }
            });
            session2.close();

            return null;
        }catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            driver.close();
//        }
        return Result.ok();
    }
}
