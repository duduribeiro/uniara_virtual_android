package br.com.uniara.virtual;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import br.com.uniara.virtual.client.core.UniaraVirtualClient;
import br.com.uniara.virtual.client.core.exceptions.NotLoggedException;
import br.com.uniara.virtual.client.models.Aluno;

public class MainActivity extends Activity {

	private Aluno aluno;
	//UI itens
	private TextView uiNomeAluno;
	
	private CarregaDadosTask taskDados;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!UniaraVirtualClient.getIntance().isLogged()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		else {
			uiNomeAluno = (TextView) findViewById(R.id.main_nome_aluno);
			taskDados = new CarregaDadosTask();
			taskDados.execute();
			setContentView(R.layout.activity_main);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public class CarregaDadosTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				aluno = UniaraVirtualClient.getIntance().informacoesAluno();
				return true;
			} catch (NotLoggedException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			taskDados = null;

			if (success) {
				uiNomeAluno = (TextView) findViewById(R.id.main_nome_aluno);
				uiNomeAluno.setText(aluno.getNome());
			}
		}

		@Override
		protected void onCancelled() {
			taskDados = null;
		}
	}
}
