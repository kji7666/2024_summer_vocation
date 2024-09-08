import java.util.*;
/** 決策樹
 * TreeRich : 聚合對象, 包含組織樹信息
 * EngineResult : 決策結果, 返回對象信息
 * TreeNode : 樹節點, 包含葉節點, 果實節點
 * TreeNodeLink : 樹節點鏈路關係
 * TreeRoot : 樹根
 */

/**
 * 聚合對象，包含組織樹信息
 */
public class TreeRich {
    private TreeRoot treeRoot; // 樹根節點
    private Map<Long, TreeNode> treeNodeMap; // 樹節點映射表

    public TreeRich(TreeRoot treeRoot, Map<Long, TreeNode> treeNodeMap) {
        this.treeRoot = treeRoot;
        this.treeNodeMap = treeNodeMap;
    }

    public TreeRoot getTreeRoot() {
        return treeRoot;
    }

    public void setTreeRoot(TreeRoot treeRoot) {
        this.treeRoot = treeRoot;
    }

    public Map<Long, TreeNode> getTreeNodeMap() {
        return treeNodeMap;
    }

    public void setTreeNodeMap(Map<Long, TreeNode> treeNodeMap) {
        this.treeNodeMap = treeNodeMap;
    }
}

/**
 * 決策結果，返回對象信息
 */
class EngineResult {
    private boolean isSuccess; // 決策是否成功
    private String result; // 決策結果信息

    public EngineResult(boolean isSuccess, String result) {
        this.isSuccess = isSuccess;
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

/**
 * 樹節點，包含葉節點，果實節點
 */
class TreeNode {
    private Long treeNodeId; // 節點ID
    private Integer nodeType; // 節點類型：1 - 果實節點，2 - 葉節點
    private String nodeValue; // 節點值
    private List<TreeNodeLink> treeNodeLinkList; // 節點鏈接列表

    public TreeNode(Long treeNodeId, Integer nodeType, String nodeValue, List<TreeNodeLink> treeNodeLinkList) {
        this.treeNodeId = treeNodeId;
        this.nodeType = nodeType;
        this.nodeValue = nodeValue;
        this.treeNodeLinkList = treeNodeLinkList;
    }

    public Long getTreeNodeId() {
        return treeNodeId;
    }

    public void setTreeNodeId(Long treeNodeId) {
        this.treeNodeId = treeNodeId;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public List<TreeNodeLink> getTreeNodeLinkList() {
        return treeNodeLinkList;
    }

    public void setTreeNodeLinkList(List<TreeNodeLink> treeNodeLinkList) {
        this.treeNodeLinkList = treeNodeLinkList;
    }
}

/**
 * 樹節點鏈路關係
 */
class TreeNodeLink {
    private Long nodeIdFrom; // 起始節點ID
    private Long nodeIdTo; // 結束節點ID
    private String ruleLimitType; // 規則限制類型
    private String ruleLimitValue; // 規則限制值

    public TreeNodeLink(Long nodeIdFrom, Long nodeIdTo, String ruleLimitType, String ruleLimitValue) {
        this.nodeIdFrom = nodeIdFrom;
        this.nodeIdTo = nodeIdTo;
        this.ruleLimitType = ruleLimitType;
        this.ruleLimitValue = ruleLimitValue;
    }

    public Long getNodeIdFrom() {
        return nodeIdFrom;
    }

    public void setNodeIdFrom(Long nodeIdFrom) {
        this.nodeIdFrom = nodeIdFrom;
    }

    public Long getNodeIdTo() {
        return nodeIdTo;
    }

    public void setNodeIdTo(Long nodeIdTo) {
        this.nodeIdTo = nodeIdTo;
    }

    public String getRuleLimitType() {
        return ruleLimitType;
    }

    public void setRuleLimitType(String ruleLimitType) {
        this.ruleLimitType = ruleLimitType;
    }

    public String getRuleLimitValue() {
        return ruleLimitValue;
    }

    public void setRuleLimitValue(String ruleLimitValue) {
        this.ruleLimitValue = ruleLimitValue;
    }
}

/**
 * 樹根
 */
class TreeRoot {
    private Long treeId; // 樹ID
    private Long treeRootNodeId; // 樹根ID

    public TreeRoot(Long treeId, Long treeRootNodeId) {
        this.treeId = treeId;
        this.treeRootNodeId = treeRootNodeId;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public Long getTreeRootNodeId() {
        return treeRootNodeId;
    }

    public void setTreeRootNodeId(Long treeRootNodeId) {
        this.treeRootNodeId = treeRootNodeId;
    }
}
