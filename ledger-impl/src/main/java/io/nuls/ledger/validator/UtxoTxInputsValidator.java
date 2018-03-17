/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2018 nuls.io
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.ledger.validator;

import io.nuls.account.service.intf.AccountService;
import io.nuls.core.constant.ErrorCode;
import io.nuls.core.constant.TxStatusEnum;
import io.nuls.core.context.NulsContext;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.script.P2PKHScript;
import io.nuls.core.script.P2PKHScriptSig;
import io.nuls.core.script.Script;
import io.nuls.core.utils.io.NulsByteBuffer;
import io.nuls.core.validate.NulsDataValidator;
import io.nuls.core.validate.ValidateResult;
import io.nuls.ledger.entity.UtxoData;
import io.nuls.ledger.entity.UtxoInput;
import io.nuls.ledger.entity.UtxoOutput;
import io.nuls.ledger.entity.tx.AbstractCoinTransaction;
import io.nuls.ledger.script.TransferScript;

/**
 * @author Niels
 * @date 2017/11/20
 */
public class UtxoTxInputsValidator implements NulsDataValidator<UtxoData> {

    private static final UtxoTxInputsValidator INSTANCE = new UtxoTxInputsValidator();


    private UtxoTxInputsValidator() {
    }

    public static UtxoTxInputsValidator getInstance() {
        return INSTANCE;
    }

    private AccountService accountService;

    @Override
    public ValidateResult validate(UtxoData data) {

        for (int i = 0; i < data.getInputs().size(); i++) {
            UtxoInput input = data.getInputs().get(i);
            UtxoOutput output = input.getFrom();

            if (data.getTransaction().getStatus() == TxStatusEnum.CACHED) {
                if (!output.isUsable()) {
                    throw new NulsRuntimeException(ErrorCode.UTXO_STATUS_CHANGE);
                }
            } else if (data.getTransaction().getStatus() == TxStatusEnum.AGREED) {
                if (!output.isLocked()) {
                    throw new NulsRuntimeException(ErrorCode.UTXO_STATUS_CHANGE);
                }
            }

            try {
                P2PKHScriptSig scriptSig = new P2PKHScriptSig();
                scriptSig.parse(input.getScriptSig());
                Script script = output.getScript();
                if (!(script instanceof P2PKHScript)) {
                    return ValidateResult.getFailedResult(ErrorCode.DATA_ERROR);
                }
                TransferScript txScript = new TransferScript(scriptSig, (P2PKHScript) output.getScript());
                txScript.verifyScript();
            } catch (NulsException e) {
                return ValidateResult.getFailedResult(ErrorCode.DATA_ERROR);
            }
        }
        return ValidateResult.getSuccessResult();
    }


    private AccountService getAccountService() {
        if (accountService == null) {
            accountService = NulsContext.getServiceBean(AccountService.class);
        }
        return accountService;
    }
}
