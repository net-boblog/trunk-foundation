package com.tumbleweed.platform.trunk.util.hierarchical;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.LinkedHashSet;

public interface HierarchicalEntity {

    public Long getId();

    public void setId(Long id);

    public long getParentId();

    public void setParentId(long parentId);

    public int getPriority();

    public void setPriority(int priority);

    public String getSortCode();

    public void setSortCode(String sortCode);

    public String getIconCls();

    public void setIconCls(String iconCls);

    public boolean isLeaf();

    public void setLeaf(boolean leaf);

    @JsonIgnore
    public boolean isChecked();

    public void setChecked(boolean checked);

    public boolean isExpanded();

    public void setExpanded(boolean expanded);

    public LinkedHashSet<? extends HierarchicalEntity> getChildren();

    public void setChildren(LinkedHashSet<? extends HierarchicalEntity> children);

    public void addChild(HierarchicalEntity child);
}
