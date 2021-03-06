	package aed.actasnotas;
	
	import es.upm.aedlib.indexedlist.*;
	import java.util.Comparator;
	
	public class ActaNotasAED implements ActaNotas {
		private IndexedList<Calificacion> database;
	
		public ActaNotasAED() {
			this.database = new ArrayIndexedList<Calificacion>();
		}
	
		private int buscarMatricula(String matricula) {
			getCalificaciones(new MatriculaComparator());
				int ini = 0;
				int fin = this.database.size() - 1;
				int cont;
				while (ini <= fin) {
					cont = (ini + fin) / 2;
					if (database.get(cont).getMatricula().compareTo(matricula) == 0) {
						return cont;
					} else if (database.get(cont).getMatricula().compareTo(matricula) > 0) {
						fin = cont - 1;
					} else {
						ini = cont + 1;
					}
				}
				return -1;
		}
	
		public void addCalificacion(String nombre, String matricula, int nota) throws CalificacionAlreadyExistsException {
			if (buscarMatricula(matricula) == -1) {
				Calificacion add = new Calificacion(nombre, matricula, nota);
				if (database.isEmpty()) {
					database.add(0, add);
				} else {
					database.add(database.size() - 1, add);
				}
=======
package aed.actasnotas;

import es.upm.aedlib.indexedlist.*;
import java.util.Comparator;

public class ActaNotasAED implements ActaNotas {
	private IndexedList<Calificacion> database;

	public ActaNotasAED() {
		this.database = new ArrayIndexedList<Calificacion>();
	}

	private int buscarMatricula(String matricula) {
		getCalificaciones(new MatriculaComparator());
		int ini = 0;
		int fin = this.database.size() - 1;
		int cont;
		while (ini <= fin) {
			cont = (ini + fin) / 2;
			if (database.get(cont).getMatricula().compareTo(matricula) == 0) {
				return cont;
			} else if (database.get(cont).getMatricula().compareTo(matricula) > 0) {
				fin = cont - 1;
			} else {
				ini = cont + 1;
			}
		}
		return -1;
	}

	public void addCalificacion(String nombre, String matricula, int nota) throws CalificacionAlreadyExistsException {
		if (buscarMatricula(matricula) == -1) {
			Calificacion add = new Calificacion(nombre, matricula, nota);
			if (database.isEmpty()) {
				database.add(0, add);
			} else {
				throw new CalificacionAlreadyExistsException();
			}
		}
	}
	
		public void updateNota(String matricula, int nota) throws InvalidMatriculaException {
			if (buscarMatricula(matricula) == -1) {
				throw new InvalidMatriculaException();
			} else {
				database.get(buscarMatricula(matricula)).setNota(nota);
			}
		}

	public void deleteCalificacion(String matricula) throws InvalidMatriculaException {
		if (buscarMatricula(matricula) == -1 || database.isEmpty()) {
			throw new InvalidMatriculaException();
		} else {
			database.removeElementAt(buscarMatricula(matricula));
		}b
	}
	
		public Calificacion getCalificacion(String matricula) throws InvalidMatriculaException {
			if (buscarMatricula(matricula) == -1) {
				throw new InvalidMatriculaException();
			} else {
				return database.get(buscarMatricula(matricula));
			}
		}
	
		public IndexedList<Calificacion> getCalificaciones(Comparator<Calificacion> cmp) {
			IndexedList<Calificacion> copy = database;
			Calificacion temp;
			for (int i = 0; i < copy.size() - 1; i++) {
				for (int j = i + 1; j < copy.size(); j++) {
					if (cmp.compare(copy.get(i), copy.get(j)) > 0) {
						//temp = copy.get(i);
						temp = copy.set(i, copy.get(j));
						copy.set(j, temp);
					}
				}
			}
			return copy;
	}

		public IndexedList<Calificacion> getAprobados(int notaMinima) {
			int cont = 0;
			IndexedList<Calificacion> aprobados = new ArrayIndexedList<Calificacion>();
			for (int i = 0; i < database.size(); i++) {
				if (database.get(i).getNota() >= notaMinima) {
					aprobados.add(cont, database.get(i));
					cont++;
				}
			}
			return aprobados;
}
