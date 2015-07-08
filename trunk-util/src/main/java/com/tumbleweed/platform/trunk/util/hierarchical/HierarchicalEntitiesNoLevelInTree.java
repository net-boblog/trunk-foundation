package com.tumbleweed.platform.trunk.util.hierarchical;

import com.mittop.platform.soupe.core.model.HierarchicalEntity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public final class HierarchicalEntitiesNoLevelInTree {

    public static Set<? extends HierarchicalEntity> hierarchicalEntitiesInTree(
            List<? extends HierarchicalEntity> hierarchicalEntities) {
        Set<HierarchicalEntity> hierarchicalEntitiesInTree = new LinkedHashSet<HierarchicalEntity>();

        if (hierarchicalEntities != null && hierarchicalEntities.size() > 0) {
            long parentId = hierarchicalEntities.get(0).getParentId();

            for (HierarchicalEntity hierarchicalEntity : hierarchicalEntities) {
                if (hierarchicalEntity.getParentId() == parentId) {
                    hierarchicalEntitiesInTree.add(hierarchicalEntity);
                }
            }
            hierarchicalEntitiesInTree(hierarchicalEntities, hierarchicalEntitiesInTree);
        }
        return hierarchicalEntitiesInTree;
    }

    private static void hierarchicalEntitiesInTree(
            List<? extends HierarchicalEntity> hierarchicalEntities,
            Set<? extends HierarchicalEntity> hierarchicalEntitiesInTree) {
        if (hierarchicalEntitiesInTree == null) {
            return;
        }
        for (HierarchicalEntity hierarchicalEntityInTree : hierarchicalEntitiesInTree) {
            for (HierarchicalEntity hierarchicalEntity : hierarchicalEntities) {
                if (hierarchicalEntityInTree.getId().equals(hierarchicalEntity.getParentId())) {
                    hierarchicalEntityInTree.setIconCls("folder");
                    hierarchicalEntityInTree.setLeaf(false);
                    hierarchicalEntityInTree.setExpanded(true);
                    hierarchicalEntityInTree.addChild(hierarchicalEntity);
                }
            }
            hierarchicalEntitiesInTree(
                    hierarchicalEntities, hierarchicalEntityInTree.getChildren());
        }
    }
}
