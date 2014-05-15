select codigovoo,otm_aeroporto.cidade as cidadep,otm_aeroporto.cidade as cidadec,codigocompanhia,partidatemporeal,chegadatemporeal
from otm_voo, otm_aeroporto
where otm_aeroporto.idaeroporto = 1 and
  otm_voo.partidaaeroportoid = otm_aeroporto.idaeroporto and
  cidadep = otm_otm_aeroporto.cidade
  