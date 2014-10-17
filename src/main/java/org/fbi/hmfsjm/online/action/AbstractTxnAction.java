package org.fbi.hmfsjm.online.action;

import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.helper.ProjectConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    public Toa run(Tia tia) {
        try {
            return process(tia);
        } catch (Exception e) {
            logger.error(TxnRtnCode.TXN_FAILED.getTitle(), e);
            throw new RuntimeException(e.getMessage() == null ? TxnRtnCode.TXN_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected Toa process(Tia tia) throws Exception;
}
