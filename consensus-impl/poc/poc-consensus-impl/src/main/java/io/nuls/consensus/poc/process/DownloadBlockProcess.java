package io.nuls.consensus.poc.process;

import io.nuls.consensus.poc.constant.BlockContainerStatus;
import io.nuls.consensus.poc.container.BlockContainer;
import io.nuls.consensus.poc.provider.BlockQueueProvider;
import io.nuls.consensus.poc.provider.IsolatedBlocksProvider;
import io.nuls.core.utils.log.ChainLog;
import io.nuls.core.utils.log.Log;
import io.nuls.network.entity.Node;
import io.nuls.network.service.NetworkService;
import io.nuls.protocol.base.download.DownloadUtils;
import io.nuls.protocol.context.NulsContext;
import io.nuls.protocol.model.Block;

import java.io.IOException;

/**
 * Created by ln on 2018-04-18.
 */
public class DownloadBlockProcess {

    private IsolatedBlocksProvider isolatedBlocksProvider;

    private NetworkService networkService = NulsContext.getServiceBean(NetworkService.class);

    public void process(BlockContainer blockContainer) throws IOException {
        downloadBlock(blockContainer);
    }

    private boolean downloadBlock(BlockContainer blockContainer) throws IOException {
        if(blockContainer.getNode() == null) {
            Log.warn("Handling an Orphan Block Error, Unknown Source Node");
            return false;
        }

        Block preBlock = new DownloadUtils().getBlockByHash(blockContainer.getBlock().getHeader().getPreHash().getDigestHex(), blockContainer.getNode());
        if(preBlock != null) {
            ChainLog.debug("get pre block success {} - {}", preBlock.getHeader().getHeight(), preBlock.getHeader().getHash().getDigestHex());

            addBlock(new BlockContainer(preBlock, blockContainer.getNode(), BlockContainerStatus.DOWNLOADING));
            return true;
        } else {
            ChainLog.debug("get pre block fail {} - {}", blockContainer.getBlock().getHeader().getHeight() - 1, blockContainer.getBlock().getHeader().getPreHash().getDigestHex());

            //失败情况的处理，从其它所以可用的节点去获取，如果都不成功，那么就失败，包括本次失败的节点，再次获取一次
            for(Node node : networkService.getAvailableNodes()) {
                preBlock = new DownloadUtils().getBlockByHash(blockContainer.getBlock().getHeader().getPreHash().getDigestHex(), blockContainer.getNode());
                if(preBlock != null) {
                    addBlock(new BlockContainer(preBlock, node, BlockContainerStatus.DOWNLOADING));
                    ChainLog.debug("get pre block retry success {} - {}", preBlock.getHeader().getHeight() - 1, preBlock.getHeader().getPreHash().getDigestHex());

                    return true;
                }
            }
            ChainLog.debug("get pre block complete failure {} - {}", blockContainer.getBlock().getHeader().getHeight() - 1, blockContainer.getBlock().getHeader().getPreHash().getDigestHex());

            return false;
        }
    }

    private void addBlock(BlockContainer blockContainer) throws IOException {
        isolatedBlocksProvider.addBlock(blockContainer);
    }

    public void setIsolatedBlocksProvider(IsolatedBlocksProvider isolatedBlocksProvider) {
        this.isolatedBlocksProvider = isolatedBlocksProvider;
    }
}
