/**
 * MIT License
 * *
 * Copyright (c) 2017-2018 nuls.io
 * *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.protocol.model;

import io.nuls.core.validate.NulsDataValidator;
import io.nuls.protocol.utils.SmallBlockValidatorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niels
 * @date 2018/1/2
 */
public class SmallBlock extends BaseNulsData {
    private BlockHeader header;
    private List<NulsDigestData> txHashList;
    private List<Transaction> subTxList = new ArrayList<>();

    public SmallBlock() {
        List<NulsDataValidator> list = SmallBlockValidatorManager.getValidators();
        for (NulsDataValidator validator : list) {
            this.registerValidator(validator);
        }
    }

    public BlockHeader getHeader() {
        return header;
    }

    public void setHeader(BlockHeader header) {
        this.header = header;
    }

    public List<NulsDigestData> getTxHashList() {
        return txHashList;
    }

    public void setTxHashList(List<NulsDigestData> txHashList) {
        this.txHashList = txHashList;
    }

    public List<Transaction> getSubTxList() {
        return subTxList;
    }

    public void addConsensusTx(Transaction tx) {
        this.subTxList.add(tx);
    }

}