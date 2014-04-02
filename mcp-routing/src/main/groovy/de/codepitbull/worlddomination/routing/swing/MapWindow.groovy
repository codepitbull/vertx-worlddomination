package de.codepitbull.worlddomination.routing.swing

import de.codepitbull.worlddomination.routing.nav.AStarBasedPathfinder
import de.codepitbull.worlddomination.routing.nav.EnvironmentMapWithWeight
import de.codepitbull.worlddomination.routing.nav.WeightedPosition

import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.MatteBorder
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 *
 * @author Jochen Mader
 */
class MapWindow extends JPanel{

    EnvironmentMapWithWeight environmentMap
    Map<WeightedPosition, CellPane> positionCellPaneMap
    WeightedPosition start
    WeightedPosition end

    public MapWindow(EnvironmentMapWithWeight environmentMap) {
        this.environmentMap = environmentMap
        positionCellPaneMap = new HashMap<>()
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < environmentMap.sideLength; row++) {
            for (int col = 0; col < environmentMap.sideLength; col++) {
                gbc.gridx = col;
                gbc.gridy = row;

                CellPane cellPane = new CellPane(environmentMap.get(col, row));
                positionCellPaneMap.put(cellPane.position, cellPane)
                Border border = null;
                if (row < environmentMap.sideLength - 1) {
                    if (col < environmentMap.sideLength - 1) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                } else {
                    if (col < 4) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                cellPane.setBorder(border);
                add(cellPane, gbc);
            }
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(MapWindow.this);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        })
    }

    Color select(WeightedPosition pos) {
        if(!start) {
            start = pos
            return Color.BLACK
        }
        else {
            end = pos
            long startTime = System.currentTimeMillis()
            AStarBasedPathfinder routing = new AStarBasedPathfinder()
            def path = routing.route(environmentMap.get(start.x, start.y), environmentMap.get(end.x, end.y), environmentMap)
            long stop = System.currentTimeMillis()
            print("Time taken: "+(stop-startTime))
            path.each {
                EventQueue.invokeLater(new DelayedRunnable(positionCellPaneMap.get(it)))
            }
            start=null
            end=null
            return Color.CYAN
        }
    }

    private class DelayedRunnable implements Runnable {
        CellPane toDraw

        DelayedRunnable(CellPane toDraw) {
            this.toDraw = toDraw
        }

        @Override
        void run() {
            toDraw.setBackground(Color.GREEN)
        }
    }

    private class CellPane extends JPanel {

        private Color defaultBackground;
        private WeightedPosition position

        public CellPane(WeightedPosition position) {
            this.position = position
            if(position.value==1) {
                setBackground(Color.RED);
            }
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    defaultBackground = getBackground();
                    setBackground(Color.BLUE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(defaultBackground);
                }

                @Override
                void mouseClicked(MouseEvent e) {
                    defaultBackground = MapWindow.this.select(position)
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }
    }
}
