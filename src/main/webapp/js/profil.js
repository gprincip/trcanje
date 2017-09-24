angular.module('profil',[])
.controller('appController', ['$q' ,'$scope' ,'$http' ,'$window' , function($q, $scope, $http, $window){
	
		$scope.prijatelji = null; //oni koje ja pratim
		$scope.pratioci = null; //oni koji me prate
		$scope.aktivnosti = [];
		$scope.ulogovaniKorisnik = null;
		$scope.trenutniKorisnikUsername = null;
		$scope.trenutnaStrana = 1;
		$scope.ukupanBrojAktivnosti = 0;
		
		$scope.getUlogovaniKorisnik = function(){
			var q = $q.defer();
			setTimeout(function(){
				$http.get("/trcanje/services/rest/ulogovaniKorisnik").then(function(response){
					$scope.ulogovaniKorisnik = response.data;
					console.info("getUlogovaniKorisnik: " +$scope.ulogovaniKorisnik);
					q.resolve(response.data);
				}, function(response){
					
				});
			},100);
		return q.promise;
		}
		
		$scope.getPrijateljiPoStranama = function(username, velicinaStrane, strana){
			
			$http.get("/trcanje/services/rest/getPrijateljiPoStranama?username="+username+"&velicinaStrane=" + velicinaStrane + "&strana=" + strana).then(function(response){
				$scope.prijatelji = response.data;
				
				$http.get("/trcanje/services/rest/getPratiociPoStranama?username="+username+"&velicinaStrane=" + velicinaStrane + "&strana=" + strana).then(function(response2){
					$scope.pratioci = response2.data;
				});
				
				return null;
			}, function(response){
				return null;
			});
			
		}

		 
		$scope.otvoriProfil = function(username){
				$window.open("/trcanje/services/rest/korisnik/" + username, "_self");
		}
		
		$scope.getAktivnosti = function(velicinaStrane, strana, username){
			var q = $q.defer();
			setTimeout(function(){
				
				$http.get("/trcanje/services/rest/getAktivnostiKorisnikaPoStranama?velicinaStrane=" + velicinaStrane + "&strana=" + strana + "&username=" + username).then(function(response){
					q.resolve(response.data);
				}, function(response){
					
				});
				
			},100);
			
			return q.promise;
			
		}
		
		$scope.ucitajAktivnosti = function(velicinaStrane, strana, username){
			$scope.getAktivnosti(velicinaStrane, strana, username).then(function(response){
				$scope.aktivnosti = response;
				
			})
		}
		
		$scope.lajkujpomocna = function(username, aktivnost_id){
			var q = $q.defer();
			setTimeout(function(){
			$http.post("/trcanje/services/rest/lajkuj?username=" + username + "&aktivnost_id=" + aktivnost_id).then(function(response){
			q.resolve(response);
			});
			},100);
			return q.promise;
		}
		
		$scope.brojLajkova = function(aktivnost_id){
			var q = $q.defer();
			setTimeout(function(){
				$http.get("/trcanje/services/rest/brojLajkova/" + aktivnost_id).then(function(response){
					q.resolve(response.data);
				});
			},100);
			return q.promise;
		}
		
		$scope.lajkuj = function(username, aktivnost_id){
			var lajk = 0;
				$scope.lajkujpomocna(username, aktivnost_id).then(function(response){
					lajk = response.data;
				}).then(function(response2){
					$scope.brojLajkova(aktivnost_id).then(function(response3){
						
						for(var i=0; i<$scope.aktivnosti.length; i++){
							if($scope.aktivnosti[i].aktivnost.id == aktivnost_id){
								$scope.aktivnosti[i].brojLajkova += lajk;
							}
						}
						
					});
				});
			
			
		}
		
		$scope.ucitajJos = function(username){
			$scope.getAktivnosti(3 , $scope.trenutnaStrana+1 , username).then(function(response){
				$scope.trenutnaStrana++;
				
				for(var i = 0; i<response.length; i++){
					$scope.aktivnosti.push(response[i]);
				}

			});
		}
		
		$scope.getBrojAktivnostiKorisnika = function(username){

			$http.get("/trcanje/services/rest/getBrojAktivnostiKorisnika?username="+username).then(function(response){
				$scope.ukupanBrojAktivnosti = response.data;
			});

	}
		
		
		$scope.podesiDugmePrati = function(username){
			$scope.getUlogovaniKorisnik().then(function(response){
			}).then(function(response2){

				$scope.getBrojAktivnostiKorisnika(username);
					
				}).then(function(response){
					
					$http.get("/trcanje/services/rest/daLiSuPrijatelji?username1=" + $scope.ulogovaniKorisnik.username + "&username2="+ username).then(function(response3){
						
						var btn = document.getElementById("btn_prati");
											
						if(response3.data){
							btn.value = "Odprati korisnika "+username;
						}else{
							btn.value = "Zaprati korisnika "+username;
						}
						
					}, function(response4){
						
					});

			})

		}
		
		$scope.prati = function(username1, username2){
			var btn = document.getElementById("btn_prati");

			if(btn.value == ("Zaprati korisnika " + username2)){
			
			$http.post("/trcanje/services/rest/dodajPrijatelja?username1=" + username1 + "&username2=" + username2).then(function(response){
				document.getElementById("btn_prati").value="Odprati korisnika "+username2;
			}, function(response){
				
			});
			
			}else if(btn.value == ("Odprati korisnika " + username2)){
				
				$http.post("/trcanje/services/rest/obrisiPrijatelja?username1=" + username1 + "&username2=" + username2).then(function(response){
					document.getElementById("btn_prati").value="Zaprati korisnika "+username2;
				}, function(response){
					
				});
				
			}
		}
		
		//*******************Pozivi funkcija***********************************

		/*$scope.getUlogovaniKorisnik().then(function(response){
			$scope.getBrojAktivnostiKorisnika(response.username);
		});*/
			
	}]);