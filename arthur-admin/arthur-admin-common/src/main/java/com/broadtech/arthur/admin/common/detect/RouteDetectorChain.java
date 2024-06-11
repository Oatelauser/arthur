package com.broadtech.arthur.admin.common.detect;

import com.google.common.base.Preconditions;

/**
 * @author Machenike
 * date 2022/10/31
 * @version 1.0.0
 */
public class RouteDetectorChain {

    private AbstractDetector head;
    private AbstractDetector tail;

    public void addNext(AbstractDetector abstractDetector) {
        if (head == null) {
            head = abstractDetector;
            tail = abstractDetector;
        }
        Preconditions.checkNotNull(abstractDetector, "The current detector is empty");
        this.tail.next = abstractDetector;
        this.tail      = abstractDetector;
    }

    public void detection(CheckAble checkAble) throws RuntimeException{
        head.detection(checkAble);
    }




}
