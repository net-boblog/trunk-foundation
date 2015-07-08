package com.tumbleweed.platform.trunk.util.hierarchical;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class HierarchicalEntitiesUtil {

    public static List<? extends HierarchicalEntity> sort(
            List<? extends HierarchicalEntity> entities) throws Exception {
        Set<HierarchicalEntity> sortedEntitiesInTree = new LinkedHashSet<HierarchicalEntity>();

        int priority = 1;
        long sortCode = 1001;
        for (HierarchicalEntity entity : entities) {
            if (entity.getParentId() == 0) {
                entity.setPriority(priority);
                entity.setSortCode(String.valueOf(sortCode));
                sortedEntitiesInTree.add(entity);

                priority++;
                sortCode++;
            }
        }

        generateSortCode(entities, sortedEntitiesInTree);

        List<HierarchicalEntity> sortedEntitiesInList = new ArrayList<HierarchicalEntity>();

        treeToList(sortedEntitiesInList, sortedEntitiesInTree);

        return sortedEntitiesInList;
    }

    private static void generateSortCode(
            List<? extends HierarchicalEntity> entities, Set<? extends HierarchicalEntity> sortedEntitiesInTree) {

        for (HierarchicalEntity sortedEntityInTree : sortedEntitiesInTree) {
            int priority = 1;
            String parentSortCode = sortedEntityInTree.getSortCode();
            long sortCode = 1001;
            for (HierarchicalEntity entity : entities) {
                if (sortedEntityInTree.getId() != null
                        && entity.getParentId() == sortedEntityInTree.getId().longValue()) {
                    entity.setPriority(priority);
                    entity.setSortCode(parentSortCode + sortCode);
                    sortedEntityInTree.addChild(entity);

                    priority++;
                    sortCode++;
                }
            }

            if (sortedEntityInTree.getChildren() != null && sortedEntityInTree.getChildren().size() > 0) {
                generateSortCode(entities, sortedEntityInTree.getChildren());
            }
        }
    }

    private static void treeToList(
            List<HierarchicalEntity> entities, Set<? extends HierarchicalEntity> sortedEntitiesInTree) {
        for (HierarchicalEntity sortedEntityInTree : sortedEntitiesInTree) {
            entities.add(sortedEntityInTree);

            if (sortedEntityInTree.getChildren() != null && sortedEntityInTree.getChildren().size() > 0) {
                treeToList(entities, sortedEntityInTree.getChildren());
            }
        }
    }

    public static Set<? extends HierarchicalEntity> hierarchicalEntitiesInTree(
            List<? extends HierarchicalEntity> hierarchicalEntities) {
        Set<HierarchicalEntity> hierarchicalEntitiesInTree = new LinkedHashSet<HierarchicalEntity>();

        if (hierarchicalEntities != null && hierarchicalEntities.size() > 0) {
            long parentId = hierarchicalEntities.get(0).getParentId();

            for (HierarchicalEntity hierarchicalEntity : hierarchicalEntities) {
                if (hierarchicalEntity.getParentId() == parentId) {
                    hierarchicalEntity.setLeaf(true);
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

                    hierarchicalEntity.setLeaf(true);
                    hierarchicalEntityInTree.addChild(hierarchicalEntity);
                }
            }

            hierarchicalEntitiesInTree(
                    hierarchicalEntities, hierarchicalEntityInTree.getChildren());
        }
    }
}
