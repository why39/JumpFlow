package com.hxy.modules.cas;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import javax.servlet.http.HttpServletRequest;
/**
 6.	 * 创 建 人 ： wangsheng 创建日期：2019年11月
 7.	 */
public class CasutilLogin { /**
 9.	 * 从cas中获取用户名
 10.	 *
 11.	 * @param request
 12.	 * @return
 13.	 */
public static String getAccountNameFromCas(HttpServletRequest request) {
   Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
   if(assertion!= null){
           AttributePrincipal principal = assertion.getPrincipal();
            return principal.getName();
            }else return null;
        }
}

