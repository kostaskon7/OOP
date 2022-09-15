#ifndef _GRAPH_HPP_ 
#define _GRAPH_HPP_
#include <vector>
#include <queue>
#include <list>
#include <limits>
#include <cmath>
#include <iostream>
#include <climits>
#include <algorithm>
#include <fstream>
#include <exception>
using namespace std;

template <class T> struct Edge;
template <class T> class Graph;


////////////////////////////////////Vertex////////////////////////////////////


template <class T>
class Vertex {
	T info;
	list<Edge<T>  > adj;
	bool visited;
	int pos;
	bool processing;
	double dist;
public:
	Vertex(T in);
	friend class Graph<T>;

	void addEdg(Vertex<T> *dest, double w);
	bool rmvEdgTo(Vertex<T> *d);

	T getInfo() const;

	int getDist() const;

	bool operator<(const Vertex<T> vertex);
	

	Vertex* path;

};


template <class T>
struct vertex_greater_than {
    bool operator()(Vertex<T> * a, Vertex<T> * b) const {
        return a->getDist() > b->getDist();
    }
};


template <class T>
bool Vertex<T>::rmvEdgTo(Vertex<T> *d) {
	typename list<Edge<T> >::iterator it= adj.begin();
	typename list<Edge<T> >::iterator ite= adj.end();
	while (it!=ite) {
		if (it->dest == d) {
			adj.erase(it);
			return true;
		}
		else it++;
	}
	return false;
}

template <class T>
Vertex<T>::Vertex(T in): info(in), visited(false), processing(false), dist(0) {
	path = NULL;
}


template <class T>
void Vertex<T>::addEdg(Vertex<T> *dest, double w) {
	Edge<T> edgeD(this->info,dest,w);
	typename list<Edge<T> >::iterator its= adj.begin();
    typename list<Edge<T> >::iterator it= adj.end();
	if(adj.size()!=0){
		while(it!=its&&(*its).dest->pos<dest->pos){
			its++;
		}
	}
	adj.insert(its, edgeD);
}


template <class T>
T Vertex<T>::getInfo() const {
	return this->info;
}

template <class T>
int Vertex<T>::getDist() const {
	return this->dist;
}






////////////////////////////////////Edge////////////////////////////////////



template <class T>
struct Edge {
	T from;
	T to;
	Vertex<T> * dest;
	double weight;
public:
	Edge(T in,Vertex<T> *d, double w, double f=0);
	double getWeight() const;
	bool operator<(const Edge<T> &other) const;
	bool operator>(const Edge<T>& e) const;
    template<typename U>
    friend std::ostream& operator<<(std::ostream& out, const Edge<U>& e);

	friend class Graph<T>;
	friend class Vertex<T>;
};

template <class T>
Edge<T>::Edge(T in,Vertex<T> *d, double w, double f):from(in),to(d->getInfo()), dest(d), weight(w){}

template <class T>
double Edge<T>::getWeight() const {
	return weight;
}

template<typename T>
std::ostream& operator<<(std::ostream& out, const Edge<T>& e) {
  out << e.from << " -- " << e.to << " (" << e.dist << ")";
  return out;
}

template <class T>
bool Edge<T>::operator<(const Edge<T> &other) const {
	return this->weight < other.weight;
}

template <class T>
struct edge_greater_than {
    bool operator()(Edge<T> a, Edge<T>  b) const {
        return a.getWeight() > b.getWeight();
    }
};


////////////////////////////////////Graph////////////////////////////////////

template <class T>
class Graph {
	list<Vertex<T> *> AdjList;
	void dfs(Vertex<T> *v, list<T> &res) const;
	int Directed,cap;

public:
	Graph(bool isDirected=true,int capacity=2);
	bool contains(const T& info);
	bool addVtx(const T &in);
	bool addEdg(const T &from, const T &to, double w);
	bool rmvVtx(const T &info);
	bool rmvEdg(const T &from, const T &to);
	list<T> dfs(const T& info) const;
	list<T> bfs(const T& info) const;
	Vertex<T>* getVertex(const T &v) const;		
	list<T> getPath(const T &start, const T &dest);
	list<T> bellman_ford(const T &from,const T &to);
	list<T> dijkstra(const T &from,const T &to);
	list<Edge<T> > mst();
	int union_find(const T & from, list<T> & arr);
	void union_set(const T & from, const T & to, list<T> & arr);
	bool print2DotFile(char *filename) const;


	
	struct NegativeGraphCycle  : public std::exception{
		
		const char * what () const throw (){
				return "Negative Graph Cycle!";
			}
	}Neg;

};

template <class T>
Graph<T>::Graph(bool isDirected,int capacity) {
	Directed=isDirected;
	cap=capacity;
}
template <class T>
bool Graph<T>::contains(const T& info){
	typename list<Vertex<T>*>::iterator it= AdjList.begin();
	typename list<Vertex<T>*>::iterator ite= AdjList.end();
	for (; it!=ite; it++){
		if((*it)->info==info){
			return true;
		}
	}
	return false;
}


template <class T>
bool Graph<T>::addVtx(const T &info) {
	typename list<Vertex<T>*>::iterator it= AdjList.begin();
	typename list<Vertex<T>*>::iterator ite= AdjList.end();
	for (; it!=ite; it++)
		if ((*it)->info == info) return false;
	Vertex<T> *v1 = new Vertex<T>(info);
	v1->pos=AdjList.size();
	AdjList.push_back(v1);
	return true;
}

template <class T>
bool Graph<T>::rmvVtx(const T &info) {
	typename list<Vertex<T>*>::iterator it= AdjList.begin();
	typename list<Vertex<T>*>::iterator ite= AdjList.end();
	for (; it!=ite; it++) {
		if ((*it)->info == info) {
			Vertex<T> * v= *it;
			AdjList.erase(it);
			typename list<Vertex<T>*>::iterator it1= AdjList.begin();
			typename list<Vertex<T>*>::iterator it1e= AdjList.end();
			for (; it1!=it1e; it1++) {
				(*it1)->rmvEdgTo(v);
			}


			delete v;
			return true;
		}
	}
	return false;
}


template <class T>
bool Graph<T>::addEdg(const T &from, const T &to, double w) {
	typename list<Vertex<T>*>::iterator it= AdjList.begin();
	typename list<Vertex<T>*>::iterator ite= AdjList.end();
	int found=0;
	Vertex<T> *vS, *vD;
	while (found!=2 && it!=ite ) {
		if ( (*it)->info == from )
			{ vS=*it; found++;}
		if ( (*it)->info == to )
			{ vD=*it; found++;}
		it ++;
	}
	if (found!=2) return false;
	typename list<Edge<T> >::iterator it_edg= (vS->adj).begin();
	typename list<Edge<T> >::iterator ite_edg= (vS->adj).end();
	for (; it_edg !=ite_edg; it_edg++){
		if((*it_edg).dest->info==to){
			return false;
		}
	}
	vS->addEdg(vD,w);

	return true;
}





template <class T>
bool Graph<T>::rmvEdg(const T &from, const T &to) {
	typename list<Vertex<T>*>::iterator it= AdjList.begin();
	typename list<Vertex<T>*>::iterator ite= AdjList.end();
	int found=0;
	Vertex<T> *vS, *vD;
	while (found!=2 && it!=ite ) {
		if ( (*it)->info == from )
			{ vS=*it; found++;}
		if ( (*it)->info == to )
			{ vD=*it; found++;}
		it ++;
	}
	if (found!=2)
		return false;


	return vS->rmvEdgTo(vD);
}




template <class T>
list<T> Graph<T>::dfs(const T& info) const {
	typename list<Vertex<T>*>::const_iterator it= AdjList.begin();
	typename list<Vertex<T>*>::const_iterator ite= AdjList.end();
	for (; (*it)->info !=info; it++){    }
	list<T> res;

	 dfs(*it,res);

     it=AdjList.begin();
    for (; it !=ite; it++){
		(*it)->visited=false;
    }

	return res;
}

template <class T>
void Graph<T>::dfs(Vertex<T> *v,list<T> &res) const {
	v->visited = true;
	res.push_back(v->info);
	typename list<Edge<T> >::iterator it= (v->adj).begin();
	typename list<Edge<T> >::iterator ite= (v->adj).end();
	for (; it !=ite; it++)
	    if ( it->dest->visited == false ){
	    	dfs(it->dest, res);
	    }
}

template <class T>
list<T> Graph<T>::bfs(const T& info) const {
	list<T> res;
	queue<Vertex<T> *> q;
	typename list<Vertex<T>*>::const_iterator it= AdjList.begin();
	typename list<Vertex<T>*>::const_iterator ite= AdjList.end();
	for (; it!=ite; it++) {
		if((*it)->info==info){
			break;
		}
	}

	q.push(*it);
	(*it)->visited = true;
	while (!q.empty()) {
		Vertex<T> *v1 = q.front();
		q.pop();
		res.push_back(v1->info);
		typename list<Edge<T> >::iterator it=v1->adj.begin();
		typename list<Edge<T> >::iterator ite=v1->adj.end();
		for (; it!=ite; it++) {
			Vertex<T> *d = it->dest;
			if (d->visited==false) {
				d->visited=true;
				q.push(d);
			}
		}
	}
	typename list<Vertex<T>*>::const_iterator b= AdjList.begin();
	typename list<Vertex<T>*>::const_iterator e= AdjList.end();
	for (; b!=e; b++) {
        (*b)->visited=false;
    }

	return res;
}

template <class T>
Vertex<T>* Graph<T>::getVertex(const T &v) const {
	typename list<Vertex<T>*>::const_iterator it= AdjList.begin();
	typename list<Vertex<T>*>::const_iterator ite= AdjList.end();
	for(;it!=ite; it++)
		if ((*it)->info == v) 
			return (*it);
	return NULL;
}



template<class T>
list<T> Graph<T>::getPath(const T &start, const T &dest){

	list<T> lst;
	Vertex<T>* v = getVertex(dest);

	lst.push_front(v->info);
	while ( v->path != NULL &&  v->path->info != start) {
		v = v->path;
		lst.push_front(v->info);
	}
	if( v->path != NULL )
		lst.push_front(v->path->info);


	list<T> res;
	while( !lst.empty() ) {
		res.push_back( lst.front() );
		lst.pop_front();
	}
	return res;
}



template<class T>
list<T> Graph<T>::bellman_ford(const T &from,const T &to) {

	typename list<Vertex<T>*>::const_iterator it= AdjList.begin();
	for(unsigned int i = 0; i < AdjList.size();it++, i++) {
		(*it)->path = NULL;
		(*it)->dist = INT_MAX;
	}

	Vertex<T>* v = getVertex(from);
	v->dist = 0;
	queue< Vertex<T>* > q;
	q.push(v);

	for(int m=0;!q.empty();m++ ) {
		v = q.front(); 
		q.pop();
		for(unsigned int i = 0; i < v->adj.size(); i++) {
			typename list<Edge<T> >::iterator it_edg=(*v).adj.begin();
			for(unsigned int k=0;k<i;k++){
				it_edg++;
			}
			Vertex<T>* w = (*it_edg).dest;
			if(v->dist + (*it_edg).weight < w->dist) {
			
				w->dist = v->dist + (*it_edg).weight;
				if(w->dist<=-1024){
					throw Neg;
				}
				w->path = v;
				q.push(w);
			}
		}

		
	}

	return(getPath(from,to));
}




//Wrong on few occasions
template<class T>
list<T> Graph<T>::dijkstra(const T &from,const T &to) {
	
	typename list<Vertex<T>*>::const_iterator it= AdjList.begin();
	for(unsigned int i = 0; i < AdjList.size();it++, i++) {
		(*it)->path = NULL;
		(*it)->dist = INT_MAX;
		(*it)->processing = false;
	}

	Vertex<T>* v = getVertex(from);
	v->dist = 0;
	vector< Vertex<T>* > pq;
	pq.push_back(v);
	make_heap(pq.begin(), pq.end());

	while( !pq.empty() ) {

		v = pq.front();
		pop_heap(pq.begin(), pq.end());
		pq.pop_back();

		for(unsigned int i = 0; i < v->adj.size(); i++) {
			typename list<Edge<T>>::iterator it_edg=(*v).adj.begin();
			for(unsigned int k=0;k<i;k++){
				it_edg++;
			}
			Vertex<T>* w = (*it_edg).dest;

			if(v->dist + (*it_edg).weight < w->dist ) {

				w->dist = v->dist + (*it_edg).weight;
				w->path = v;

				if(!w->processing)
				{
					w->processing = true;
					pq.push_back(w);
				}

				make_heap (pq.begin(),pq.end(),vertex_greater_than<T>());
			}
		}
	}
	list<T> result=getPath(from,to);

	return(result);
}


template<class T>
int Graph<T>::union_find(const T & from, list<T> & arr){
	unsigned int i;
	typename list<Vertex<T>*>::const_iterator it= AdjList.begin();
	for( i = 0; i < AdjList.size();it++, i++) {
		if(from==(*it)->info){
			break;
		}
	}
	typename list<T>::const_iterator ite=arr.begin();
	for(unsigned int k=0;k<i;k++){
		ite++;
	}
    if((*ite) == from){
        return i;
    }
    return union_find((*ite), arr);
}


template<class T>
void Graph<T>::union_set(const T & from, const T & to, list<T> & arr){
	unsigned int from_pos;
	int i=union_find(to, arr);   
	typename list<T>::iterator it=arr.begin();
	for(int k=0;k<i;k++){
		it++;
	}	
	
	typename list<Vertex<T>*>::iterator it_from= AdjList.begin();
	for( from_pos = 0; from_pos < AdjList.size();it_from++, from_pos++) {
		if(from==(*it_from)->info){
			break;
		}
	}
	typename list<T>::iterator ite=arr.begin();
	for(unsigned int k=0;k<from_pos;k++){
		ite++;
	}

    arr.erase(it);
	it=arr.begin();
	for(int k=0;k<i;k++){
		it++;
	}	
	arr.insert(it,*ite);
}

//Based on Kruskal's minimum spanning tree
template<typename T>
std::list<Edge<T> > Graph<T>::mst(){
    list<Edge<T> > mst;
    list<T> arr;
	
	list<Edge<T> > edges;
	
	typename list<Vertex<T>*>::const_iterator it= AdjList.begin();
	for(unsigned int i = 0; i < AdjList.size();it++, i++) {
		typename list<Edge<T> >::iterator it_edg=(*it)->adj.begin();
		for(unsigned int k = 0; k < (*it)->adj.size();it_edg++, k++) {
			    edges.push_back(*it_edg);
		}
	}
	
    for(const auto & it: AdjList){
        arr.push_back(it->info);
    }

	edges.sort();
    for(const auto & it : edges){
        T from = it.from;
        T to = it.to;
        double w = it.weight;
        if( union_find(from, arr) != union_find(to, arr)){
            mst.push_back(Edge<T>(from, getVertex(to), w));
            union_set(from, to, arr);
        }
    }
    return mst;
}



template <class T>
bool Graph<T>::print2DotFile(char *filename) const {

	typename list<Vertex<T>*>::const_iterator loop= AdjList.begin();
	typename list<Vertex<T>*>::const_iterator end= AdjList.end();
	string point;
    
    ofstream graphFile(filename, ios::out | ios:: trunc);
    if (!graphFile.is_open()) {
        cerr << "An error accured. " << endl;
        graphFile.close();
        return(false);
    }
	if(Directed){
		graphFile << "digraph g {\n";
		point=" -> ";
	}
	else{
		graphFile << "graph g {\n";
		point=" -- ";
	}
		


    for(;loop!=end;loop++){
		typename list<Edge<T> >::iterator it_edg=(*loop)->adj.begin();
		typename list<Edge<T> >::iterator ite_edg=(*loop)->adj.end();
		for(;it_edg!=ite_edg;it_edg++){
			if(Directed||(*loop)->pos<(*it_edg).dest->pos){
				graphFile << (*loop)->info << point;
				graphFile << (*it_edg).dest->info << ";\n";
			}
		}
    }

    graphFile << "}";
	return(true);
}


#endif
