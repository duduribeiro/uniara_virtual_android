package br.com.uniara.virtual;

import br.com.uniara.virtual.core.UniaraVirtualClient;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(UniaraVirtualClient.getIntance().isLogged()) {
			System.out.println("teste");
		}else {
			System.out.println("não logado");
		}
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
