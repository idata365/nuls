package io.nuls.network.service.impl;

import io.nuls.kernel.model.BaseNulsData;
import io.nuls.network.constant.NetworkParam;
import io.nuls.network.entity.BroadcastResult;
import io.nuls.network.entity.Node;
import io.nuls.network.service.NetworkService;

import java.util.Collection;
import java.util.Map;

public class NetworkServiceImpl implements NetworkService {
    @Override
    public void removeNode(String nodeId) {

    }

    @Override
    public Node getNode(String nodeId) {
        return null;
    }

    @Override
    public Map<String, Node> getNodes() {
        return null;
    }

    @Override
    public Collection<Node> getAvailableNodes() {
        return null;
    }

    @Override
    public BroadcastResult sendToAllNode(BaseNulsData event, boolean asyn) {
        return null;
    }

    @Override
    public BroadcastResult sendToAllNode(BaseNulsData event, Node excludeNode, boolean asyn) {
        return null;
    }

    @Override
    public BroadcastResult sendToNode(BaseNulsData event, Node node, boolean asyn) {
        return null;
    }

    @Override
    public BroadcastResult sendToGroup(BaseNulsData event, String groupName, boolean asyn) {
        return null;
    }

    @Override
    public BroadcastResult sendToGroup(BaseNulsData event, String groupName, String excludeNodeId, boolean asyn) {
        return null;
    }

    @Override
    public boolean reset() {
        return false;
    }

    @Override
    public NetworkParam getNetworkParam() {
        return null;
    }
}
