package br.unisc.pdm.trabalho.view.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

import br.unisc.pdm.trabalho.R;

public class BitmapLoader {

    public void loadBitmap(String path, ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        //Android has limited the max size of bitmap to be rendered. So I'll resize it.
        //Bitmap too large to be uploaded into a texture (3264x2448, max=2048x2048)
        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

        //FIXME Não sei porque mais minhas fotos tiradas no celular ficam de cabeça para baixo. Fiz a gambiarra abaixo, por enquanto.
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotated = Bitmap.createBitmap(scaled , 0, 0, scaled.getWidth(), scaled.getHeight(), matrix, true);

        imageView.setImageBitmap(rotated);
    }

    public void loadDefaultPhoto(ImageView imageView) {
        imageView.setImageResource(R.drawable.default_user);
    }

}