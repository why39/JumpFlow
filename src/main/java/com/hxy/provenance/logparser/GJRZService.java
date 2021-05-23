package com.hxy.provenance.logparser;

import com.hxy.modules.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GJRZService {
    @Autowired
    private GJRZDao logDao;

    @Autowired
    GJAJService caseService;

    public void fuse(String bmsah, String bmsah2) {
        //查询两个案件的日志，修改日志的bmsah为新的。并且对于相同的流程环节日志只能插入一次

        List<GJRZEntity> rzlist1 = logDao.queryList(bmsah);
        List<GJRZEntity> rzlist2 = logDao.queryList(bmsah2);

        if (rzlist1 == null || rzlist2 == null) {
            return;
        }

        List<GJRZEntity> taskNodeList1 = rzlist1.stream().filter(rz -> rz.getEJFL().startsWith(GJRZEntity.LC_PREFIX)).collect(Collectors.toList());
        List<GJRZEntity> taskNodeList2 = rzlist2.stream().filter(rz -> rz.getEJFL().startsWith(GJRZEntity.LC_PREFIX)).collect(Collectors.toList());

        taskNodeList1.addAll(taskNodeList2);
        rzlist1.addAll(rzlist2);

        List<GJRZEntity> taskNodeList = new ArrayList<>();
        taskNodeList1.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<GJRZEntity>(Comparator.comparing(f -> f.getRZMS()))), ArrayList::new))
                .forEach(e -> taskNodeList.add(e));

        //1. 插入一条mysql数据
        GJAJEntity oldAj1 = caseService.queryObject(bmsah);
        GJAJEntity oldAj2 = caseService.queryObject(bmsah2);

        String newBMSAH = bmsah + "_" + bmsah2;
        String newTYSAH = oldAj1.getTYSAH().substring(0, 9) + oldAj2.getTYSAH().substring(0, 6);
        GJAJEntity gjajEntity = new GJAJEntity();
        gjajEntity.setBMSAH(newBMSAH);
        gjajEntity.setTYSAH(newTYSAH);
        gjajEntity.setAJMC(oldAj1.getAJMC() + "2");
        gjajEntity.setCBDW_MC(oldAj1.getCBDW_MC());
        gjajEntity.setAJLB_MC(oldAj1.getAJLB_MC());
        gjajEntity.setCJSJ(oldAj1.getCJSJ());

        //20210517 Guizhou
        gjajEntity.setYWMC(oldAj1.getYWMC());
        gjajEntity.setCBJCG(oldAj1.getCBJCG());
        gjajEntity.setCBJCGSF(oldAj1.getCBJCGSF());
        gjajEntity.setBATDMC(oldAj1.getBATDMC());
        gjajEntity.setIS_COMPLETE(0);

        caseService.saveAJ(gjajEntity);

        //先插入环节节点日志
        taskNodeList.stream().forEach(e->{
            GJRZEntity gjrzEntity = new GJRZEntity();
            gjrzEntity.setCZRM(e.getCZRM());
            gjrzEntity.setBMSAH(newBMSAH);
            gjrzEntity.setZHXGSJ(e.getZHXGSJ());
            gjrzEntity.setEJFL(e.getEJFL());
            gjrzEntity.setID(UUID.randomUUID().toString());
            gjrzEntity.setRZMS(e.getRZMS());
            logDao.save(gjrzEntity);
        });

        //再插入其他类型的日志
        rzlist1.stream().forEach(e->{

            long contains = taskNodeList1.stream().filter(a -> a.getID().equals(e.getID())).count();
            if (contains == 0) {
                GJRZEntity gjrzEntity = new GJRZEntity();
                gjrzEntity.setCZRM(e.getCZRM());
                gjrzEntity.setBMSAH(newBMSAH);
                gjrzEntity.setZHXGSJ(e.getZHXGSJ());
                gjrzEntity.setEJFL(e.getEJFL());
                gjrzEntity.setID(UUID.randomUUID().toString());
                gjrzEntity.setRZMS(e.getRZMS());
                logDao.save(gjrzEntity);
            }
        });

        caseService.parseLog(newBMSAH);
    }
}
