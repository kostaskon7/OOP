
#ifndef _GRAPH_UI_
#define _GRAPH_UI_

#include <iterator>
#include <cstring>
using namespace std;


template <typename T>
int graphUI() {
  
  string option, line;
  double sum;
  bool digraph = false;
  
  cin >> option;
  if(!option.compare("digraph"))
    digraph = true;
  Graph<T> g(digraph);
  
  while(true) {
    
    std::stringstream stream;
    cin >> option;
    
    if(!option.compare("av")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if(g.addVtx(vtx))
        cout << "av " << vtx << " OK\n";
      else
        cout << "av " << vtx << " NOK\n";
    }
    else if(!option.compare("rv")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if(g.rmvVtx(vtx))
	cout << "rv " << vtx << " OK\n";
      else
	cout << "rv " << vtx << " NOK\n";
    }
    else if(!option.compare("ae")) {
      getline (std::cin, line);
      stream << line;
      T vtx1(stream);
      T vtx2(stream);
      stream << line;
      int w;
      stream>>w;
      if(g.addEdg(vtx1,vtx2,w)){
      	cout << "ae " << vtx1 <<" "<<vtx2<<" OK\n";
      	}
            else{
      	cout << "ae " << vtx1 <<" "<<vtx2<<  " NOK\n";
      	}
      	if(!digraph){
      		g.addEdg(vtx2,vtx1,w);
      	}
       
    }
    else if(!option.compare("re")) {
      std::getline (std::cin, line,' ');
      stream << line;
      T vtx1(stream);
      std::getline (std::cin, line,' ');
      stream << line;
      T vtx2(stream);

      if(g.rmvEdg(vtx1,vtx2))
      	cout << "re " << vtx1 <<" "<<vtx2<< " OK\n";
            else{
      	cout << "re " << vtx1<<" "<<vtx2<<  " NOK\n";
      	}
    	if(!digraph){
    		if(g.rmvEdg(vtx2,vtx1))
    	          cout << "re " << vtx2 <<" "<<vtx1<<  " OK\n";
                  else
    	          cout << "re " << vtx2 <<" "<<vtx1<<  " NOK\n";
    	}
    }
    else if(!option.compare("dot")) {
      
    }
    else if(!option.compare("bfs")) {
      
      cout << "\n----- BFS Traversal -----\n";
      std::getline (std::cin, line);
      stream << line;
      T vtx(stream);
      std::list<T> list=g.bfs(vtx);
        typename list<T >::iterator it=list.begin();
		typename list<T>::iterator ite=list.end();
        cout<<(*it);
        it++;
		for (; it!=ite; it++) {
            cout<<" -> "<<(*it);
        }

      cout << "\n-------------------------\n";
    }
    else if(!option.compare("dfs")) {
      
      cout << "\n----- DFS Traversal -----\n";
      
      std::getline (std::cin, line);
      stream << line;
      T vtx(stream);
      std::list<T> list=g.dfs(vtx);
        typename list<T >::iterator it=list.begin();
		typename list<T>::iterator ite=list.end();
        cout<<(*it);
        it++;
		for (; it!=ite; it++) {
            cout<<" -> "<<(*it);
        }
      
      cout << "\n-------------------------\n";
    }
    else if(!option.compare("dijkstra")) {
      getline(std::cin,line);

      stream << line;
      T from(stream);
      T to(stream);
      std::list<T> list=g.dijkstra(from,to);

      typename list<T >::iterator it=list.begin();
      typename list<T >::iterator ite=list.end();
      cout << "Dijkstra (" << from << " - " << to <<"): ";
	  if(list.size()==1){
		  cout<<endl;
		  continue;
	  }
      cout<<(*it);
      it++;
      for (; it!=ite; it++) {
            cout<<", "<<(*it);
      }
      cout<<endl;
    }
    else if(!option.compare("bellman-ford")) {
      std::getline(std::cin, line);
      stream << line;
      T from(stream);
      T to(stream);
      cout << "Bellman-Ford (" << from << " - " << to <<"): ";
	  try{
		  std::list<T> list=g.bellman_ford(from,to);
		  typename list<T >::iterator it=list.begin();
		  typename list<T >::iterator ite=list.end();
		  if(list.size()<=1){
			  cout<<endl;
			  continue;
		  }
		  cout<<(*it);
		  it++;
		  for (; it!=ite; it++) {
				cout<<", "<<(*it);
		  }
		  cout<<endl;      
		  }
	  catch(std::exception& e){
		cout << e.what() ;
	    cout<<endl;      
		continue;
	  }

      
    }
    else if(!option.compare("mst")) {

      cout << "\n--- Min Spanning Tree ---\n";
	  std::list<Edge<T>> list=g.mst();

      typename list<Edge<T>>::iterator it=list.begin();
      typename list<Edge<T> >::iterator ite=list.end();
	  if(list.size()<=1){
		  cout<<endl;
		  continue;
	  }

      for (; it!=ite; it++) {
		  sum+=(*it).weight;
          cout<<(*it).from <<" -- "<<(*it).to<<" ("<<(*it).weight<<")\n";
      }
      cout << "MST Cost: " << sum << endl;
    }
    else if(!option.compare("q")) {
     // cout << "bye bye...\n";
      return 0;
    }
    else if(!option.compare("#")) {
      string line;
      getline(cin,line);
      //cerr << "Skipping line: " << line << endl;
    }
    else {
      cout << "INPUT ERROR\n";
      return -1;
    }
  }
  return -1;  
}

#endif
