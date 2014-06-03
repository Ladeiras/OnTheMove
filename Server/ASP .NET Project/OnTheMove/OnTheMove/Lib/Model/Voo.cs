﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnTheMove.Lib.Model
{
    public class Voo
    {
        public Decimal Id
        {
            get;
            set;
        }

        public Decimal CodigoVoo
        {
            get;
            set;
        }

        public string CodigoCompanhia
        {
            get;
            set;
        }

        public Decimal PartidaAeroportoId
        {
            get;
            set;
        }

        public Decimal ChegadaAeroportoId
        {
            get;
            set;
        }

        public String PartidaCidade
        {
            get;
            set;
        }

        public String ChegadaCidade
        {
            get;
            set;
        }

        public DateTime PartidaTempoEstimado
        {
            get;
            set;
        }

        public DateTime ChegadaTempoEstimado
        {
            get;
            set;
        }

        public DateTime PartidaTempoReal
        {
            get;
            set;
        }

        public DateTime ChegadaTempoReal
        {
            get;
            set;
        }

        public Decimal Terminal
        {
            get;
            set;
        }

        public DateTime CheckinInicio
        {
            get;
            set;
        }

        public DateTime CheckinFim
        {
            get;
            set;
        }

        public Decimal PortaEmbarque
        {
            get;
            set;
        }

        public DateTime Embarque
        {
            get;
            set;
        }

        public Decimal TapeteBagagem
        {
            get;
            set;
        }

        public DateTime Bagagem
        {
            get;
            set;
        }

        public Decimal PortaDesembarque
        {
            get;
            set;
        }

        public DateTime Desembarque
        {
            get;
            set;
        }
    }
}