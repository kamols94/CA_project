package CylindricalArm_;


import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.Box;




/////////////// G Ł Ó W N A   K L A S A ///////////////
public class CylindricalArm extends JFrame implements ActionListener, KeyListener{
/////////////// Z M I E N N E ///////////////
     private Timer zegar;
     private float ramie_wys=0.025f;
     private double ramie_kat=0.0f;
     private float ramie_zas=0.0f;
     private boolean[] klawisz = { false, false, false, false, false, false, false, false };
     private TransformGroup tg_ramie_rp1;
     private Transform3D t3d_ramie_rp1;
     private TransformGroup tg_ramie_tp2;
     private Transform3D t3d_ramie_tp2;
     private TransformGroup tg_ramie_tp3;
     private Transform3D t3d_ramie_tp3;
     private TransformGroup tg_prymityw;
     private Transform3D t3d_prymityw;
     private Vector3f v3f_prymityw = new Vector3f(0.25f,1.0f,-0.25f);
     private boolean mag_prymityw = false;
     private Button przyciskStart = new Button("START: NORMAL");
     private Button przyciskNagraj = new Button("START: RECORD");
     private Button przyciskOdtworz = new Button("PLAY RECORD");
     private boolean[] tryb = { false, false, false };
     private final int rozmiar=6000;
     public int cykl=0;
     private int decymacja=0;
     private float[] poz_wys = new float[rozmiar];
     private double[] poz_kat = new double[rozmiar];
     private float[] poz_zas = new float[rozmiar];
     private boolean[] poz_pre = new boolean[rozmiar];
     private boolean[] poz_rel = new boolean[rozmiar];
     private Audio dzwiek_start = new Audio("sounds/mlg.mp3");
     private Audio dzwiek_wys = new Audio("sounds/mlg.mp3");
     private Audio dzwiek_kat = new Audio("sounds/mlg.mp3");
     private Audio dzwiek_zas = new Audio("sounds/mlg.mp3");
     private Audio dzwiek_mag = new Audio("sounds/mag.mp3");
/////////////// F U N K C J E ///////////////
     //Wykrywanie Kolizji
     public boolean Kolizja(Vector3f v3f){
        boolean isKolizja = false;
        double Ax = (0.11f+ramie_zas)*Math.cos(ramie_kat);
        double Ay = ramie_wys+0.075/Math.sqrt(2);
        double Az = -(0.11f+ramie_zas)*Math.sin(ramie_kat);
        double Bx = -(1.0f-0.1f-ramie_zas)*Math.cos(ramie_kat);
        double By = ramie_wys+0.075/Math.sqrt(2);
        double Bz = (1.0f-0.1f-ramie_zas)*Math.sin(ramie_kat);
        double Cx = v3f.x;
        double Cy = v3f.y;
        double Cz = v3f.z;
        double AB = 1.01;
        double AC = Math.sqrt((Ax-Cx)*(Ax-Cx)+(Ay-Cy)*(Ay-Cy)+(Az-Cz)*(Az-Cz));
        double BC = Math.sqrt((Bx-Cx)*(Bx-Cx)+(By-Cy)*(By-Cy)+(Bz-Cz)*(Bz-Cz));
        if(AC<=0.11 || BC<0.11){
                isKolizja = true;
            }
        if(AC<=1.00 && BC<=1.01){
            double cos_kat = (AB*AB+AC*AC-BC*BC)/(2*AB*AC);
            double sin_kat = Math.sqrt(1-cos_kat*cos_kat);
            double odleglosc = AC*sin_kat;
            if(odleglosc<=0.12){
                isKolizja = true;
            }
        }
        return isKolizja;
     }
     
     //Obiekt w zasięgu manesu - sprawdzenie
     public boolean MagnesRange(Vector3f v3f,float mr_wys, double mr_kat, float mr_zas){
        boolean isRange = false;
        double Ax = (0.11f+mr_zas)*Math.cos(mr_kat);
        double Ay = mr_wys+0.075/Math.sqrt(2);
        double Az = -(0.11f+mr_zas)*Math.sin(mr_kat);
        double Bx = -(1.0f-0.1f-mr_zas)*Math.cos(mr_kat);
        double By = mr_wys+0.075/Math.sqrt(2);
        double Bz = (1.0f-0.1f-mr_zas)*Math.sin(mr_kat);
        double Cx = v3f.x;
        double Cy = v3f.y;
        double Cz = v3f.z;
        double AB = 1.01;
        double AC = Math.sqrt((Ax-Cx)*(Ax-Cx)+(Ay-Cy)*(Ay-Cy)+(Az-Cz)*(Az-Cz));
        double BC = Math.sqrt((Bx-Cx)*(Bx-Cx)+(By-Cy)*(By-Cy)+(Bz-Cz)*(Bz-Cz));
        if(AC<=0.12 && BC>1.11 && Ay>0.09f){
                isRange = true;
            }
        return isRange;
     }
     //
     
/////////////// M A I N : KONSTRUKTOR ///////////////
    CylindricalArm(){

        setTitle("Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        


        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(800,600));

        add(canvas3D);
        canvas3D.addKeyListener(this);
        zegar = new Timer(1,this);
        
        pack();
        setVisible(true);
        
       JPanel panel = new JPanel();
       panel.add(przyciskStart);
       add("North",panel);
       przyciskStart.addActionListener(this);
       przyciskStart.addKeyListener(this);
       panel.add(przyciskNagraj);
       add("North",panel);
       przyciskNagraj.addActionListener(this);
       przyciskNagraj.addKeyListener(this);
       panel.add(przyciskOdtworz);
       add("North",panel);
       przyciskOdtworz.addActionListener(this);
       przyciskOdtworz.addKeyListener(this);


        BranchGroup scena = utworzScene();
	    scena.compile();

        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        
/////////////// O B S E R W A T O R ///////////////
        
       OrbitBehavior OB = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL); //sterowanie myszką
       BoundingSphere BS = new BoundingSphere(new Point3d(0,0,0),100);
       OB.setSchedulingBounds(BS);
       ViewingPlatform VP = simpleU.getViewingPlatform();
       VP.setViewPlatformBehavior(OB);

       Transform3D przesuniecie_obserwatora = new Transform3D();
       przesuniecie_obserwatora.set(new Vector3f(0.0f,0.5f,3.0f));
       
        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        simpleU.addBranchGraph(scena);
        



}
/////////////// O B S Ł U G A   K L A W I A T U R Y ///////////////
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:      klawisz[0] = true; break;
                    case KeyEvent.VK_DOWN:    klawisz[1] = true; break;
                    case KeyEvent.VK_LEFT:    klawisz[2] = true; break;
                    case KeyEvent.VK_RIGHT:   klawisz[3] = true; break;
                    case KeyEvent.VK_W:       klawisz[4] = true; break;
                    case KeyEvent.VK_S:       klawisz[5] = true; break;
                    case KeyEvent.VK_C:       klawisz[6] = true; break;
                    case KeyEvent.VK_V:       klawisz[7] = true; break;
                }
        
    }
        public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_UP:      klawisz[0] = false; break;
                    case KeyEvent.VK_DOWN:    klawisz[1] = false; break;
                    case KeyEvent.VK_LEFT:    klawisz[2] = false; break;
                    case KeyEvent.VK_RIGHT:   klawisz[3] = false; break;
                    case KeyEvent.VK_W:       klawisz[4] = false; break;
                    case KeyEvent.VK_S:       klawisz[5] = false; break;
                    case KeyEvent.VK_C:       klawisz[6] = false; break;
                    case KeyEvent.VK_V:       klawisz[7] = false; break;
                }
        
    }
        public void keyTyped(KeyEvent e) {}

    public void actionPerformed(ActionEvent e) {
/////////////// O B S Ł U G A   P R Z Y C I S K Ó W ///////////////
        if(e.getSource()==przyciskStart){
            if(!zegar.isRunning()) {zegar.start(); dzwiek_start.play();}
            tryb[0]=true;
            tryb[1]=false;
            tryb[2]=false;
            decymacja=0;
            
        }
        if(e.getSource()==przyciskNagraj){
            tryb[0]=false;
            tryb[1]=true;
            tryb[2]=false;
            cykl=0;
            decymacja=0;
        }
        if(e.getSource()==przyciskOdtworz){
            tryb[0]=false;
            tryb[1]=false;
            tryb[2]=true;
            cykl=0;
            decymacja=0;
        }
/////////////// N A G R Y W A N I E ///////////////
        if(tryb[1]){
            if(klawisz[6] && cykl<(rozmiar-1)){poz_pre[cykl]=true;}
            if(klawisz[7] && cykl<(rozmiar-1)){poz_rel[cykl]=true;}
            if((decymacja%10)==0 && cykl<(rozmiar-1)){
                poz_wys[cykl]=ramie_wys;
                poz_kat[cykl]=ramie_kat;
                poz_zas[cykl]=ramie_zas;
                System.out.println(cykl + " : " + poz_wys[cykl]);
                System.out.println(cykl + " : " + poz_kat[cykl]);
                System.out.println(cykl + " : " + poz_zas[cykl]);
                cykl++;
            }
            decymacja++;
        }
        
/////////////// O D T W A R Z A N I E ///////////////
        if(tryb[2]){
           if(poz_pre[cykl]){
               if(MagnesRange(v3f_prymityw,poz_wys[cykl],poz_kat[cykl],poz_zas[cykl])){
                   mag_prymityw=true;
               }
           }
           if(poz_rel[cykl]){
               mag_prymityw=false;
           }
           
            if(mag_prymityw){
                v3f_prymityw.setX((0.22f+poz_zas[cykl])*(float)Math.cos(poz_kat[cykl]));
                v3f_prymityw.setY(poz_wys[cykl]+0.075f/(float)Math.sqrt(2));
                v3f_prymityw.setZ(-(0.22f+poz_zas[cykl])*(float)Math.sin(poz_kat[cykl]));
                t3d_prymityw.set(v3f_prymityw);
                tg_prymityw.setTransform(t3d_prymityw);
            }
            
            if(v3f_prymityw.y>0.1f && !mag_prymityw)
            {
                
                v3f_prymityw.setY(v3f_prymityw.y-0.001f);
                t3d_prymityw.set(v3f_prymityw);
                tg_prymityw.setTransform(t3d_prymityw);
            }
            if(mag_prymityw && (decymacja%75)==0){dzwiek_mag.play();}
            if((decymacja%10)==0 && cykl<(rozmiar-1)){
                t3d_ramie_rp1.rotY(poz_kat[cykl]);
                tg_ramie_rp1.setTransform(t3d_ramie_rp1);
          
                t3d_ramie_tp2.set(new Vector3f(0f, poz_wys[cykl],0f));
                tg_ramie_tp2.setTransform(t3d_ramie_tp2);
            
                t3d_ramie_tp3.set(new Vector3f(poz_zas[cykl],0f,0f));
                tg_ramie_tp3.setTransform(t3d_ramie_tp3);
                cykl++;
            }
            decymacja++;
        }
        /////////////// W Y Ś W I E T L A N I E   B I E Ż Ą C Y C H   R U C H Ó W ///////////////
        if(tryb[0]||tryb[1]){
        if(klawisz[0] && ramie_wys<0.85f){
               ramie_wys+=0.001f;
               if(Kolizja(v3f_prymityw) && !mag_prymityw){ramie_wys-=0.001f;}
           }
           if(klawisz[1] && ((ramie_wys>0.025f && !mag_prymityw) || (ramie_wys+0.075/(float)Math.sqrt(2)>0.1f && mag_prymityw))){
               ramie_wys-=0.001f;
               if(Kolizja(v3f_prymityw) && !mag_prymityw){ramie_wys+=0.001f;}
           }
           if(klawisz[2] && ramie_kat<6){
               ramie_kat+=0.01;
               if(Kolizja(v3f_prymityw) && !mag_prymityw){ramie_kat-=0.01f;}
           }
           if(klawisz[3] && ramie_kat>0){
               ramie_kat-=0.01;
               if(Kolizja(v3f_prymityw) && !mag_prymityw){ramie_kat+=0.01f;}
           }
           if(klawisz[4]&& ramie_zas<0.8f){
               ramie_zas+=0.001f;
               if(Kolizja(v3f_prymityw) && !mag_prymityw){ramie_zas-=0.001f;}
           }
           if(klawisz[5] && ramie_zas>0){
               ramie_zas-=0.001f;
               if(Kolizja(v3f_prymityw) && !mag_prymityw){ramie_zas+=0.001f;}
           }
           if(klawisz[6]){
               if(MagnesRange(v3f_prymityw,ramie_wys,ramie_kat,ramie_zas)){
                   mag_prymityw=true;
               }
           }
           if(klawisz[7]){
               mag_prymityw=false;
           }
           
            if(mag_prymityw){
                if((decymacja%75)==0){dzwiek_mag.play();}
                decymacja++;
                v3f_prymityw.setX((0.22f+ramie_zas)*(float)Math.cos(ramie_kat));
                v3f_prymityw.setY(ramie_wys+0.075f/(float)Math.sqrt(2));
                v3f_prymityw.setZ(-(0.22f+ramie_zas)*(float)Math.sin(ramie_kat));
                t3d_prymityw.set(v3f_prymityw);
                tg_prymityw.setTransform(t3d_prymityw);
            }
            
            if(v3f_prymityw.y>0.1f && !mag_prymityw)
            {
                
                v3f_prymityw.setY(v3f_prymityw.y-0.001f);
                t3d_prymityw.set(v3f_prymityw);
                tg_prymityw.setTransform(t3d_prymityw);
            }
            
            t3d_ramie_rp1.rotY(ramie_kat);
            tg_ramie_rp1.setTransform(t3d_ramie_rp1);
          
            t3d_ramie_tp2.set(new Vector3f(0f, ramie_wys,0f));
            tg_ramie_tp2.setTransform(t3d_ramie_tp2);
            
            t3d_ramie_tp3.set(new Vector3f(ramie_zas,0f,0f));
            tg_ramie_tp3.setTransform(t3d_ramie_tp3);
            }
        
    }

    BranchGroup utworzScene(){

        BranchGroup wezel_scena = new BranchGroup();
/////////////// P R Y M I  T Y W ///////////////
        tg_prymityw = new TransformGroup();
        wezel_scena.addChild(tg_prymityw);
        t3d_prymityw = new Transform3D();
        tg_prymityw.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_prymityw.setCapability( TransformGroup.ALLOW_TRANSFORM_READ);
        t3d_prymityw.set(v3f_prymityw);
        tg_prymityw.setTransform(t3d_prymityw);
        Appearance  app_prymityw = new Appearance();
        app_prymityw.setColoringAttributes(new ColoringAttributes(1.0f,0.0f,0.0f,ColoringAttributes.NICEST));
        Sphere prymityw = new Sphere(0.1f,app_prymityw);
        tg_prymityw.addChild(prymityw);

/////////////// O T O C Z E N I E ///////////////
        BranchGroup otoczenie = new BranchGroup();
        wezel_scena.addChild(otoczenie);
                //niebo
        Texture na_niebo = new TextureLoader("tekstura/cegla.jpg",this).getTexture();
        
        Appearance w_nieba = new Appearance();
        w_nieba.setTexture(na_niebo);
        
        Sphere sfera = new Sphere(25.0f,Sphere.GENERATE_NORMALS_INWARD|Sphere.GENERATE_TEXTURE_COORDS,100,w_nieba);
        
        //ziemia
        Texture na_podloge = new TextureLoader("tekstura/toplel.jpg",this).getTexture();
        
        Appearance w_podlogi = new Appearance();
        w_podlogi.setTexture(na_podloge);
        
        com.sun.j3d.utils.geometry.Box podloga = new com.sun.j3d.utils.geometry.Box
                (25.0f,0.0001f,25.0f,
                com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS|
                com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS,
                 w_podlogi);
        otoczenie.addChild(sfera);
        otoczenie.addChild(podloga);
        
/////////////// R A M I E   R O B O T A : DRZEWO ///////////////
        BranchGroup ramie_robota = new BranchGroup();
        wezel_scena.addChild(ramie_robota);
        //DRZEWO: RAMIE CZĘŚĆ 1
        TransformGroup tg_ramie_tp1 = new TransformGroup();
        ramie_robota.addChild(tg_ramie_tp1);
        tg_ramie_rp1 = new TransformGroup();
        t3d_ramie_rp1 = new Transform3D();
        tg_ramie_rp1.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_ramie_rp1.setCapability( TransformGroup.ALLOW_TRANSFORM_READ);
        tg_ramie_tp1.addChild(tg_ramie_rp1);
        //DRZEWO: RAMIE CZĘŚĆ 2
        tg_ramie_tp2 = new TransformGroup();
        t3d_ramie_tp2 = new Transform3D();
        tg_ramie_tp2.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_ramie_tp2.setCapability( TransformGroup.ALLOW_TRANSFORM_READ);
        tg_ramie_rp1.addChild(tg_ramie_tp2);
        TransformGroup tg_ramie_rp2 = new TransformGroup();
        tg_ramie_tp2.addChild(tg_ramie_rp2);
        //DRZEWO: RAMIE CZĘŚĆ 3
        tg_ramie_tp3 = new TransformGroup();
        t3d_ramie_tp3 = new Transform3D();
        tg_ramie_tp3.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_ramie_tp3.setCapability( TransformGroup.ALLOW_TRANSFORM_READ);
        tg_ramie_rp2.addChild(tg_ramie_tp3);
        TransformGroup tg_ramie_rp3 = new TransformGroup();
        tg_ramie_tp3.addChild(tg_ramie_rp3);
        
/////////////// R A M I E   R O B O T A : MODEL GRAFICZNY ///////////////
        //Appearance
        Appearance  app_pj = new Appearance();
        app_pj.setColoringAttributes(new ColoringAttributes(0.5f,0.5f,0.5f,ColoringAttributes.NICEST));
        Appearance  app_jarzmo = new Appearance();
        app_jarzmo.setColoringAttributes(new ColoringAttributes(1.0f,0.0f,0.0f,ColoringAttributes.NICEST));
        Appearance  app_belka = new Appearance();
        app_belka.setColoringAttributes(new ColoringAttributes(0.0f,1.0f,0.0f,ColoringAttributes.NICEST));
        Appearance  app_jb = new Appearance();
        app_jb.setColoringAttributes(new ColoringAttributes(0.0f,0.0f,1.0f,ColoringAttributes.NICEST));
        Appearance  app_wys = new Appearance();
        app_wys.setColoringAttributes(new ColoringAttributes(1.0f,1.0f,1.0f,ColoringAttributes.NICEST));
        Appearance  app_mag = new Appearance();
        app_mag.setColoringAttributes(new ColoringAttributes(0.0f,1.0f,0.0f,ColoringAttributes.NICEST));
        //"Przytwierdzenie"
            TransformGroup tg_mg_pj = new TransformGroup();
            Transform3D t3d_mg_pj = new Transform3D();
            t3d_mg_pj.set(new Vector3f(0f,0.0075f,0f));
            tg_mg_pj.setTransform(t3d_mg_pj);
            com.sun.j3d.utils.geometry.Box pj = new com.sun.j3d.utils.geometry.Box(0.15f, 0.015f, 0.15f, app_pj);
            tg_mg_pj.addChild(pj);
            tg_ramie_tp1.addChild(tg_mg_pj);
        //"Jarzmo"
            TransformGroup tg_mg_jarzmo = new TransformGroup();
            Transform3D t3d_mg_jarzmo = new Transform3D();
            t3d_mg_jarzmo.set(new Vector3f(0f,0.0125f,0f));
            tg_mg_jarzmo.setTransform(t3d_mg_jarzmo);
            Cylinder jarzmo = new Cylinder(0.1f, 0.025f, app_jarzmo);
            tg_mg_jarzmo.addChild(jarzmo);
            tg_ramie_rp1.addChild(tg_mg_jarzmo);
        //"Belki"
            //#1
            TransformGroup tg_mg_belka1 = new TransformGroup();
            Transform3D t3d_mg_belka1 = new Transform3D();
            t3d_mg_belka1.set(new Vector3f(0.075f/(float)Math.sqrt(2),0.5f,0.075f/(float)Math.sqrt(2)));
            tg_mg_belka1.setTransform(t3d_mg_belka1);
            Cylinder belka1 = new Cylinder(0.02f, 1.0f, app_belka);
            tg_mg_belka1.addChild(belka1);
            tg_ramie_rp1.addChild(tg_mg_belka1);
            //#2
            TransformGroup tg_mg_belka2 = new TransformGroup();
            Transform3D t3d_mg_belka2 = new Transform3D();
            t3d_mg_belka2.set(new Vector3f(-0.075f/(float)Math.sqrt(2),0.5f,0.075f/(float)Math.sqrt(2)));
            tg_mg_belka2.setTransform(t3d_mg_belka2);
            Cylinder belka2 = new Cylinder(0.02f, 1.0f, app_belka);
            tg_mg_belka2.addChild(belka2);
            tg_ramie_rp1.addChild(tg_mg_belka2);
            //#3
            TransformGroup tg_mg_belka3 = new TransformGroup();
            Transform3D t3d_mg_belka3 = new Transform3D();
            t3d_mg_belka3.set(new Vector3f(-0.075f/(float)Math.sqrt(2),0.5f,-0.075f/(float)Math.sqrt(2)));
            tg_mg_belka3.setTransform(t3d_mg_belka3);
            Cylinder belka3 = new Cylinder(0.02f, 1.0f, app_belka);
            tg_mg_belka3.addChild(belka3);
            tg_ramie_rp1.addChild(tg_mg_belka3);
            //#4
            TransformGroup tg_mg_belka4 = new TransformGroup();
            Transform3D t3d_mg_belka4 = new Transform3D();
            t3d_mg_belka4.set(new Vector3f(0.075f/(float)Math.sqrt(2),0.5f,-0.075f/(float)Math.sqrt(2)));
            tg_mg_belka4.setTransform(t3d_mg_belka4);
            Cylinder belka4 = new Cylinder(0.02f, 1.0f, app_belka);
            tg_mg_belka4.addChild(belka4);
            tg_ramie_rp1.addChild(tg_mg_belka4);
            //#5
            TransformGroup tg_mg_belka5 = new TransformGroup();
            Transform3D t3d_mg_belka5_tra = new Transform3D();
            t3d_mg_belka5_tra.set(new Vector3f(0f,0.98f, 0f));
            Transform3D t3d_mg_belka5_rot1 = new Transform3D();
            t3d_mg_belka5_rot1.rotY(-Math.PI/4);
            t3d_mg_belka5_tra.mul(t3d_mg_belka5_rot1);
            Transform3D t3d_mg_belka5_rot2 = new Transform3D();
            t3d_mg_belka5_rot2.rotZ(Math.PI/2);
            t3d_mg_belka5_tra.mul(t3d_mg_belka5_rot2);
            Cylinder belka5 = new Cylinder(0.02f, 0.19f, app_belka);
            tg_mg_belka5.addChild(belka5);
            tg_mg_belka5.setTransform(t3d_mg_belka5_tra);
            tg_ramie_rp1.addChild(tg_mg_belka5);
            //#6
            TransformGroup tg_mg_belka6 = new TransformGroup();
            Transform3D t3d_mg_belka6_tra = new Transform3D();
            t3d_mg_belka6_tra.set(new Vector3f(0f,0.98f, 0f));
            Transform3D t3d_mg_belka6_rot1 = new Transform3D();
            t3d_mg_belka6_rot1.rotY(Math.PI/4);
            t3d_mg_belka6_tra.mul(t3d_mg_belka6_rot1);
            Transform3D t3d_mg_belka6_rot2 = new Transform3D();
            t3d_mg_belka6_rot2.rotZ(Math.PI/2);
            t3d_mg_belka6_tra.mul(t3d_mg_belka6_rot2);
            Cylinder belka6 = new Cylinder(0.02f, 0.19f, app_belka);
            tg_mg_belka6.addChild(belka6);
            tg_mg_belka6.setTransform(t3d_mg_belka6_tra);
            tg_ramie_rp1.addChild(tg_mg_belka6);
        //"Glowica"
            //#1
            TransformGroup tg_mg_jb1 = new TransformGroup();
            Transform3D t3d_mg_jb1 = new Transform3D();
            t3d_mg_jb1.set(new Vector3f(0.075f/(float)Math.sqrt(2), 0.075f/(float)Math.sqrt(2), 0.075f/(float)Math.sqrt(2)));
            tg_mg_jb1.setTransform(t3d_mg_jb1);
            Cylinder jb1 = new Cylinder(0.025f, 0.15f/(float)Math.sqrt(2), app_jb);
            tg_mg_jb1.addChild(jb1);
            tg_ramie_tp2.addChild(tg_mg_jb1);
            //#2
            TransformGroup tg_mg_jb2 = new TransformGroup();
            Transform3D t3d_mg_jb2 = new Transform3D();
            t3d_mg_jb2.set(new Vector3f(-0.075f/(float)Math.sqrt(2), 0.075f/(float)Math.sqrt(2), 0.075f/(float)Math.sqrt(2)));
            tg_mg_jb2.setTransform(t3d_mg_jb2);
            Cylinder jb2 = new Cylinder(0.025f, 0.15f/(float)Math.sqrt(2), app_jb);
            tg_mg_jb2.addChild(jb2);
            tg_ramie_tp2.addChild(tg_mg_jb2);
            //#3
            TransformGroup tg_mg_jb3 = new TransformGroup();
            Transform3D t3d_mg_jb3 = new Transform3D();
            t3d_mg_jb3.set(new Vector3f(-0.075f/(float)Math.sqrt(2), 0.075f/(float)Math.sqrt(2), -0.075f/(float)Math.sqrt(2)));
            tg_mg_jb3.setTransform(t3d_mg_jb3);
            Cylinder jb3 = new Cylinder(0.025f, 0.15f/(float)Math.sqrt(2), app_jb);
            tg_mg_jb3.addChild(jb3);
            tg_ramie_tp2.addChild(tg_mg_jb3);
            //#4
            TransformGroup tg_mg_jb4 = new TransformGroup();
            Transform3D t3d_mg_jb4 = new Transform3D();
            t3d_mg_jb4.set(new Vector3f(0.075f/(float)Math.sqrt(2), 0.075f/(float)Math.sqrt(2), -0.075f/(float)Math.sqrt(2)));
            tg_mg_jb4.setTransform(t3d_mg_jb4);
            Cylinder jb4 = new Cylinder(0.025f, 0.15f/(float)Math.sqrt(2), app_jb);
            tg_mg_jb4.addChild(jb4);
            tg_ramie_tp2.addChild(tg_mg_jb4);
            //###
            TransformGroup tg_mg_jb = new TransformGroup();
            Transform3D t3d_mg_jb = new Transform3D();
            t3d_mg_jb.set(new Vector3f(0f, 0.075f/(float)Math.sqrt(2), 0f));
            Transform3D t3d_mg_jb_rot = new Transform3D();
            t3d_mg_jb_rot.rotZ(Math.PI/2);
            t3d_mg_jb.mul(t3d_mg_jb_rot);
            Cylinder jb = new Cylinder(0.075f/(float)Math.sqrt(2),(0.15f/(float)Math.sqrt(2))+0.05f, app_jb);
            tg_mg_jb.addChild(jb);
            tg_mg_jb.setTransform(t3d_mg_jb);
            tg_ramie_tp2.addChild(tg_mg_jb);            
        //"Wysiegnik"
            TransformGroup tg_mg_wys = new TransformGroup();
            Transform3D  t3d_mg_wys = new Transform3D();
            t3d_mg_wys.rotZ(Math.PI/2);
            Transform3D pion_tra = new Transform3D();
            pion_tra.set(new Vector3f(0.075f/(float)Math.sqrt(2),0.4f, 0f));
            t3d_mg_wys.mul(pion_tra);
            tg_mg_wys.setTransform(t3d_mg_wys);
            Cylinder wys = new Cylinder(0.02f, 1.0f, app_wys);
            tg_mg_wys.addChild(wys);
            tg_ramie_rp3.addChild(tg_mg_wys);
        //"Magnes"
            TransformGroup tg_mg_mag = new TransformGroup();
            Transform3D  t3d_mg_mag = new Transform3D();
            t3d_mg_mag.rotZ(Math.PI/2);
            Transform3D mag_tra = new Transform3D();
            mag_tra.set(new Vector3f(0.075f/(float)Math.sqrt(2),-0.085f, 0f));
            t3d_mg_mag.mul(mag_tra);
            tg_mg_mag.setTransform(t3d_mg_mag);
            Cone mag = new Cone(0.02f, 0.05f, app_mag);
            tg_mg_mag.addChild(mag);
            tg_ramie_rp3.addChild(tg_mg_mag);
        
        
        return wezel_scena;

    }

     public static void main(String args[]){
      CylindricalArm CA = new CylindricalArm();
      CA.addKeyListener(CA);

   }

}
