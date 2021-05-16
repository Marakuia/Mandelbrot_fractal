import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class FractalExplorer {
    private int size; //для отслеживания размера экрана
    private JImageDisplay img;  //для отображения фрактала
    private FractalGenerator fract;
    private Rectangle2D.Double rect; //диапазон

    public FractalExplorer(int size){
        this.size = size;
        fract = new Mandelbrot();
        rect = new Rectangle2D.Double();
        fract.getInitialRange(rect);
        img = new JImageDisplay(size, size);

    }
    public void createAndShowGUI (){
        //Создаем форму и даем ей заголовок
        img.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");

        //помещаем изображение в центр формы
        frame.add(img, BorderLayout.CENTER);

        //Создаем кнопку для сброса изображений
        JButton btnReset = new JButton("Reset");
        //Создаем слушатель для кнопки
        Reset ResListener = new Reset();
        btnReset.addActionListener(ResListener);
        //указываем расположение кнопки
        frame.add(btnReset, BorderLayout.SOUTH);

        //Создаем слушатель для щелчков мыши
        Mouse MouseListener = new Mouse();
        img.addMouseListener(MouseListener);

        //реализуем операцию закрытыя по умолчанию
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack ();  //правильное размещение содержимого окна
        frame.setVisible (true);    //содержимое делаем видимым
        frame.setResizable (false);  //запрет  изменения размера окна
    }


    public void drawFractal (){
        for (int x = 0; x<size; x++){
            for (int y = 0; y < size; y++){
                double xCoord = fract.getCoord(rect.x, rect.x + rect.width, size, x);
                double yCoord = fract.getCoord(rect.y, rect.y + rect.height, size, y);

                int numIters = fract.numIterations(xCoord, yCoord); //Вычисляем количество итераций для соответствующих
                                                                    // координат в области отображения фрактала

                if(numIters == -1)
                    img.drawPixel(x, y, 0);
                else{
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    img.drawPixel(x, y, rgbColor);
                }
            }
        }
        img.repaint();  //обновляем изображение
    }
     private class Reset implements ActionListener {

        //Создаем метод, реагирующий на нажатие кнопки
         public void actionPerformed(ActionEvent e) {
             fract.getInitialRange(rect);  //сбрасывает к изначальному диапазону
             drawFractal();                 //Перерисовывает фрактал
         }
     }


     private class Mouse extends MouseAdapter {
        public void mouseClicked(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            //отображает пиксельные кооринаты щелчка в область фрактала
            double xCoord = fract.getCoord(rect.x, rect.x + rect.width, size, x);
            double yCoord = fract.getCoord(rect.y, rect.y + rect.width, size, y);

            //вызывает метод с координатами, по которым щелкнули, и масштабом 0.5
            fract.recenterAndZoomRange(rect, xCoord, yCoord,0.5);
            drawFractal();

        }

     }
    public static void main(String[] args)
    {
        FractalExplorer ResFrat  = new FractalExplorer(600); //размер отображения
        ResFrat.createAndShowGUI();
        ResFrat.drawFractal(); //отображение начального представления фрактала
    }
}
